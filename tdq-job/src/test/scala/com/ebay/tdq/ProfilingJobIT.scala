package com.ebay.tdq

import java.sql.DriverManager
import java.util.{HashMap => JMap}

import com.ebay.sojourner.common.env.EnvironmentUtils
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.common.model.{InternalMetric, TdqEvent}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.sinks.{MemorySink, MemorySinkFunction, ProntoSink}
import com.ebay.tdq.sources.MemorySourceFactory
import com.ebay.tdq.utils.{JsonUtils, TdqConfigManager}
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
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
  name: String, config: String, events: List[TdqEvent], expects: List[InternalMetric]
) extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    setupDB(config)
    super.setup(Array.concat(args, Array("--flink.app.name", name)))
    tdqEnv.setJobName(name)
    //    val tdqConfig = JsonUtils.parseObject(config, classOf[TdqConfig])
    //    JsonUtils.toJSONString(new TdqConfig(tdqConfig.getId, tdqConfig.getName, tdqConfig.getSources, tdqConfig.getRules, Lists.newArrayList(
    //      new SinkConfig("tdq_hdfs_normal_metric", "realtime.hdfs", mapAsJavaMap(Map(
    //        "sub-type" -> "normal-metric",
    //        "rheos-services-urls" -> "https://rheos-services.qa.ebay.com",
    //        "schema-subject" -> "tdq.metric",
    //        "hdfs-path" -> "target/${flink.app.profile}/metric/normal"
    //      ))),
    //      new SinkConfig("tdq_console_normal_metric", "realtime.console", mapAsJavaMap(Map(
    //        "sub-type" -> "normal-metric",
    //        "std-name" -> "nor@mal"
    //      ))),
    //      new SinkConfig("tdq_memory_normal_metric", "realtime.memory", mapAsJavaMap(Map(
    //        "sub-type" -> "normal-metric"
    //      )))
    //    )))
    TdqConfigManager.getInstance(tdqEnv).refresh()
    val memorySink = new MemorySinkFunction(name, TdqConfigManager.getInstance(tdqEnv).getPhysicalPlans.get(0))
    MemorySink.setMemoryFunction(memorySink)
    MemorySourceFactory.setRawEventList(events.asJava)
  }

  override def stop(): Unit = {
    Assert.assertTrue(MemorySink.getMemoryFunction.asInstanceOf[MemorySinkFunction].check(expects.asJava))
  }

  def getMetric0(metricKey: String, t: Long, tags: JMap[String, String], expr: JMap[String, Double], v: Double): InternalMetric = {
    val m = new InternalMetric(metricKey, t).setValue(v)
    tags.asScala.foreach { case (k, v) =>
      m.putTag(k, v)
    }
    expr.asScala.foreach { case (k, v) =>
      m.putExpr(k, v)
    }
    m.genMetricId
  }
}

class EsProfilingJobIT(name: String, config: String, events: List[TdqEvent], expects: List[InternalMetric])
  extends ProfilingJobIT(name, config, events, expects) {
  var elasticsearchResource: TdqElasticsearchResource = _

  override def setup(args: Array[String]): Unit = {
    super.setup(args)
    elasticsearchResource = new TdqElasticsearchResource("es-test")
    elasticsearchResource.start()
  }

  override def stop(): Unit = {
    Thread.sleep(3000)
    val client: Client = elasticsearchResource.getClient
    val pattern = EnvironmentUtils.replaceStringWithPattern(
      tdqEnv.getTdqConfig.getSinks.asScala
        .find(s => s.getType.equals("realtime.pronto"))
        .map(_.getConfig.get("index-pattern").asInstanceOf[String]).get
    )
    val searchRequest = new SearchRequest(
      s"${pattern + ProntoSink.getIndexDateSuffix(expects.head.getEventTime, tdqEnv.getTimeZone)}")
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
    println("pronto=>")
    resultInPronto.foreach(println)

    Assert.assertTrue(MemorySink.getMemoryFunction.asInstanceOf[MemorySinkFunction]
      .check0(expects.asJava, resultInPronto.asJava))
    elasticsearchResource.stop()
  }
}

object ProfilingJobIT {
  def setupDB(config: String): Unit = {
    val jdbc = new JdbcEnv
    Class.forName(jdbc.getDriverClassName)
    val conn = DriverManager.getConnection(jdbc.getUrl, jdbc.getUser, jdbc.getPassword)
    conn.createStatement().execute("drop table if exists rhs_config")
    conn.createStatement().execute("CREATE TABLE `rhs_config` (`id` int auto_increment, `name` varchar(100) NOT NULL,`config` mediumtext NOT NULL,`status` varchar(10) NOT NULL DEFAULT 'ACTIVE')")
    val ps = conn.prepareStatement("INSERT INTO rhs_config (name,config) VALUES (?,?)")
    ps.setString(1, JsonUtils.parseObject(config, classOf[TdqConfig]).getName)
    ps.setString(2, config)
    ps.addBatch()
    ps.executeBatch

    val is = classOf[ProfilingJobIT].getResourceAsStream("/rhs_table.sql")
    val sql = IOUtils.toString(is)
    for (s <- sql.split(";")) {
      if (StringUtils.isNotBlank(s)) conn.createStatement().execute(s)
    }
    ps.close()
    conn.close()
  }

  def es(id: String, config: String, events: List[TdqEvent], expects: List[InternalMetric]): ProfilingJobIT = {
    val it = new EsProfilingJobIT(id, config, events, expects)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.noRestart", "true"
    ))
    it
  }

  def apply(id: String, config: String, events: List[TdqEvent], expects: List[InternalMetric]): ProfilingJobIT = {
    val it = new ProfilingJobIT(id, config, events, expects)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.noRestart", "true"
    ))
    it
  }
}
