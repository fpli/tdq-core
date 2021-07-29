package com.ebay.tdq

import java.sql.DriverManager
import java.util.{HashMap => JMap}

import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.common.model.{TdqEvent, TdqMetric}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.sinks.{MemorySink, TdqSinks}
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
  name: String, config: String, events: List[TdqEvent], expects: List[TdqMetric]
) extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    super.setup(Array.concat(args, Array("--flink.app.name", name)))
    tdqEnv.setJobName(name)
    setupDB(config)
    TdqConfigManager.getInstance(tdqEnv).refresh()
    val memorySink = new MemorySink(name, TdqConfigManager.getInstance(tdqEnv).getPhysicalPlans.get(0))
    TdqSinks.setMemoryFunction(memorySink)
    MemorySourceFactory.setRawEventList(events.asJava)
  }

  override def stop(): Unit = {
    Assert.assertTrue(TdqSinks.getMemoryFunction.asInstanceOf[MemorySink].check(expects.asJava))
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
}

class EsProfilingJobIT(name: String, config: String, events: List[TdqEvent], expects: List[TdqMetric])
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
    println("pronto=>")
    resultInPronto.foreach(println)

    Assert.assertTrue(TdqSinks.getMemoryFunction.asInstanceOf[MemorySink]
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

    val is = classOf[ProfilingJobIT].getResourceAsStream("/rhs_lkp_table.sql")
    val sql = IOUtils.toString(is)
    for (s <- sql.split(";")) {
      if (StringUtils.isNotBlank(s)) conn.createStatement().execute(s)
    }
    ps.close()
    conn.close()
  }

  def es(id: String, config: String, events: List[TdqEvent], expects: List[TdqMetric]): ProfilingJobIT = {
    val it = new EsProfilingJobIT(id, config, events, expects)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.normal-metric.pronto-index-pattern", "tdq.${flink.app.profile}.metric.normal."
    ))
    it
  }

  def apply(id: String, config: String, events: List[TdqEvent], expects: List[TdqMetric]): ProfilingJobIT = {
    val it = new ProfilingJobIT(id, config, events, expects)
    it.submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.normal-metric.mem-name", id
    ))
    it
  }
}
