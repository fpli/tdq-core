package com.ebay.tdq

import java.sql.DriverManager
import java.time.Duration
import java.util.{HashMap => JMap, List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.rules.{PhysicalPlans, TdqMetric}
import com.ebay.tdq.sinks.MemorySink
import com.ebay.tdq.utils._
import com.google.common.collect.Lists
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.connectors.elasticsearch.TdqElasticsearchResource
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.junit.Assert

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
case class ProfilingJobIT(
                           id: String, config: String, events: List[RawEvent], expects: List[TdqMetric]) extends ProfilingJob {

  def submit(): Unit = {
    // step0: prepare environment
    env = FlinkEnvFactory.create(Array[String](), true)
    tdqEnv = new TdqEnv
    setTdqEnv(tdqEnv)

    ProfilingJobIT.setupDB(config)

    // step1: build data source
    val rawEventDataStream = buildSource(env)
    // step2: normalize event to metric
    val normalizeOperator = normalizeMetric(env, rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    val collect = new MemorySink(id)
    outputTags.asScala.foreach { case (k, v) =>
      val uid = "console_out_" + k
      v.addSink(collect)
        .name(uid)
        .uid(uid)
    }
    env.execute("Tdq Job [id=" + id + "]")
    Assert.assertTrue(collect.check(expects.asJava))
  }

  def submit2Pronto(): Unit = {
    val elasticsearchResource = new TdqElasticsearchResource("es-test")
    elasticsearchResource.start()

    // step0: prepare environment
    env = FlinkEnvFactory.create(Array[String](), true)
    tdqEnv = new TdqEnv
    tdqEnv.getSinkTypes.asScala.foreach { case (_, v) =>
      v.add("pronto")
    }
    setTdqEnv(tdqEnv)

    ProfilingJobIT.setupDB(config)

    // step1: build data source
    val rawEventDataStream = buildSource(env)
    // step2: normalize event to metric
    val normalizeOperator: DataStream[TdqMetric] = normalizeMetric(env, rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    outputMetricByWindow(outputTags)

    val collect = new MemorySink(id)
    outputTags.asScala.foreach { case (k, v) =>
      val uid = "console_out_" + k
      v.addSink(collect)
        .name(uid)
        .uid(uid)
    }
    env.execute("Tdq Job [id=" + id + "]")
    Assert.assertTrue(collect.check(expects.asJava))

    Thread.sleep(1000)
    val client: Client = elasticsearchResource.getClient
    val searchRequest = new SearchRequest(s"${tdqEnv.getProntoEnv.getNormalMetricIndex(expects.head.getEventTime)}")
    val searchSourceBuilder = new SearchSourceBuilder()
    searchSourceBuilder.query(QueryBuilders.matchAllQuery())
    searchSourceBuilder.size(100)
    searchSourceBuilder.from(0)
    println(searchSourceBuilder)
    searchRequest.source(searchSourceBuilder)
    val resp = client.search(searchRequest).get()
    val resultInPronto = resp.getHits.iterator().asScala.map(f => {
      getMetric0(
        f.getSourceAsMap.get("metric_key").asInstanceOf[String],
        f.getSourceAsMap.get("event_time").asInstanceOf[Long],
        f.getSourceAsMap.get("tags").asInstanceOf[JMap[String, String]],
        f.getSourceAsMap.get("expr").asInstanceOf[JMap[String, Double]],
        f.getSourceAsMap.get("value").asInstanceOf[Double]
      )
    }).toList
    println("expects=>")
    expects.foreach(println)
    //Thread.sleep(10000000)
    println("pronto=>")
    resultInPronto.foreach(println)
    Assert.assertTrue(MemorySink.check0(resultInPronto.asJava, expects.asJava))

    elasticsearchResource.stop()
  }

  private def getMetric0(metricKey: String, t: Long, tags: JMap[String, String], expr: JMap[String, Double], v: Double): TdqMetric = {
    val m = new TdqMetric(metricKey, t).setValue(v)
    tags.asScala.foreach { case (k, v) =>
      m.putTag(k, v)
    }
    expr.asScala.foreach { case (k, v) =>
      m.putExpr(k, v)
    }
    m.genUID
  }

  private def buildSource(env: StreamExecutionEnvironment): JList[DataStream[RawEvent]] = {
    Lists.newArrayList(env.addSource(new SourceFunction[RawEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[RawEvent]): Unit = {
        Thread.sleep(1000)
        events.foreach(ctx.collect)
        //        Thread.sleep(10000000)
      }

      override def cancel(): Unit = {}
    }).name("Raw Event Src1")
      .uid("raw-event-src1")
      .slotSharingGroup("src1")
      .assignTimestampsAndWatermarks(
        WatermarkStrategy
          .forBoundedOutOfOrderness[RawEvent](Duration.ofSeconds(0))
          .withTimestampAssigner(new SojSerializableTimestampAssigner[RawEvent])
          .withIdleness(Duration.ofSeconds(1))
      )
      .slotSharingGroup("src1")
      .name("Raw Event Watermark Src1")
      .uid("raw-event-watermark-src1")
      .slotSharingGroup("src1"))
  }

  override protected def getConfigDS(env: StreamExecutionEnvironment): DataStream[PhysicalPlans] = {
    env.addSource(new RichSourceFunction[PhysicalPlans]() {
      @throws[Exception]
      override def run(ctx: SourceFunction.SourceContext[PhysicalPlans]): Unit = {
        ctx.collectWithTimestamp(ProfilingSqlParserTest.getPhysicalPlans(config), System.currentTimeMillis)
      }

      override def cancel(): Unit = {}
    }).name("Tdq Config Source")
      .uid("tdq-config-source")
      .assignTimestampsAndWatermarks(
        WatermarkStrategy.forBoundedOutOfOrderness[PhysicalPlans](Duration.ofMinutes(0))
          .withIdleness(Duration.ofSeconds(1))
      )
      .setParallelism(1)
      .name("Tdq Config Watermark Source")
      .uid("tdq-config-watermark-source")
  }
}

object ProfilingJobIT {
  def setupDB(config: String): Unit = {
    val jdbc = new JdbcEnv
    Class.forName(jdbc.getDriverClassName)
    val conn = DriverManager.getConnection(jdbc.getUrl, jdbc.getUser, jdbc.getPassword)
    conn.createStatement().execute("drop table if exists rhs_config")
    conn.createStatement().execute("CREATE TABLE `rhs_config` (`config` mediumtext NOT NULL,`status` varchar(10) NOT NULL DEFAULT 'ACTIVE')")
    val ps = conn.prepareStatement("INSERT INTO rhs_config (config) VALUES (?)")
    ps.setString(1, config)
    ps.addBatch()
    ps.executeBatch

    val is = classOf[ProfilingJobIT].getResourceAsStream("/rhs_lkp_table.sql")
    val sql = IOUtils.toString(is)
    for (s <- sql.split(";")) {
      if (StringUtils.isNotBlank(s)) conn.createStatement().execute(s)
    }
    ps.close()
  }
}
