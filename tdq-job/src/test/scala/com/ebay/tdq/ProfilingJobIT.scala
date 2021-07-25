package com.ebay.tdq

import java.sql.DriverManager
import java.time.Duration
import java.util.{HashMap => JMap, List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.planner.LkpManager
import com.ebay.tdq.rules.TdqMetric
import com.ebay.tdq.sinks.MemorySink
import com.google.common.collect.Lists
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.functions.source.SourceFunction
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
  id: String, config: String, events: List[RawEvent], expects: List[TdqMetric]
) extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    super.setup(args)
    setupDB(config)
    LkpManager.getInstance(tdqEnv.getJdbcEnv).refresh()
  }

  override def start(): Unit = {
    val memorySink = new MemorySink(id, LkpManager.getInstance(tdqEnv.getJdbcEnv).getPhysicalPlans.get(0))
    // step1: build data source
    val rawEventDataStream = buildSource()
    // step2: normalize event to metric
    val normalizeOperator = normalizeMetric(rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    outputTags.asScala.foreach { case (k, v) =>
      val uid = "console_out_" + k
      v.addSink(memorySink)
        .name(uid)
        .uid(uid)
    }
    env.execute("Tdq Job [id=" + id + "]")
    Assert.assertTrue(memorySink.check(expects.asJava))
  }

  def getMetric0(metricKey: String, t: Long, tags: JMap[String, String], expr: JMap[String, Double], v: Double): TdqMetric = {
    val m = new TdqMetric(metricKey, t).setValue(v)
    tags.asScala.foreach { case (k, v) =>
      m.putTag(k, v)
    }
    expr.asScala.foreach { case (k, v) =>
      m.putExpr(k, v)
    }
    m.genUID
  }

  def buildSource(): JList[DataStream[RawEvent]] = {
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
}

class EsProfilingJobIT(id: String, config: String, events: List[RawEvent], expects: List[TdqMetric])
  extends ProfilingJobIT(id, config, events, expects) {

  override def start(): Unit = {
    val memorySink = new MemorySink(id, LkpManager.getInstance(tdqEnv.getJdbcEnv).getPhysicalPlans.get(0))

    val elasticsearchResource = new TdqElasticsearchResource("es-test")
    elasticsearchResource.start()

    // step1: build data source
    val rawEventDataStream = buildSource()
    // step2: normalize event to metric
    val normalizeOperator: DataStream[TdqMetric] = normalizeMetric(rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    outputMetricByWindow(outputTags)

    outputTags.asScala.foreach { case (k, v) =>
      val uid = "console_out_" + k
      v.addSink(memorySink)
        .name(uid)
        .uid(uid)
    }
    env.execute("Tdq Job [id=" + id + "]")
    Assert.assertTrue(memorySink.check(expects.asJava))

    Thread.sleep(1000)
    val client: Client = elasticsearchResource.getClient
    val searchRequest = new SearchRequest(
      s"${tdqEnv.getSinkEnv.getNormalMetricIndex(expects.head.getEventTime)}")
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

    Assert.assertTrue(memorySink.check0(expects.asJava, resultInPronto.asJava))

    elasticsearchResource.stop()
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

  def es(id: String, config: String, events: List[RawEvent], expects: List[TdqMetric]): ProfilingJobIT = {
    val it = new EsProfilingJobIT(id, config, events, expects)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.normal-metric.pronto-index-pattern", "tdq.${flink.app.profile}.metric.normal."
    ))
    it
  }

  def apply(id: String, config: String, events: List[RawEvent], expects: List[TdqMetric]): ProfilingJobIT = {
    val it = new ProfilingJobIT(id, config, events, expects)
    println("+++++++================" + id)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.normal-metric.std-name", "normal"
    ))
    it
  }
}
