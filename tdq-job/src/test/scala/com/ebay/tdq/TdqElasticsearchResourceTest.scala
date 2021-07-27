package com.ebay.tdq

import java.util.{HashMap => JMap}

import com.ebay.tdq.common.model.TdqMetric
import org.apache.flink.streaming.connectors.elasticsearch.TdqElasticsearchResource
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.junit.Test

import scala.collection.JavaConverters.asScalaIteratorConverter

/**
 * @author juntzhang
 */
class TdqElasticsearchResourceTest {
  @Test
  def test_basic_es(): Unit = {
    val elasticsearchResource = new TdqElasticsearchResource("es-test")
    elasticsearchResource.start()
    val client = elasticsearchResource.getClient

    val index = "tdq-metrics-test"
    val doc1 = client.index(Requests.indexRequest.index(index).source(getJson(1, pageId = "2"))).get()
    val doc2 = client.index(Requests.indexRequest.index(index).source(getJson(2))).get()
    println(doc1)
    println(doc2)

    println(client.get(new GetRequest(index, doc1.getId)).actionGet.getSource)
    println(client.get(new GetRequest(index, doc2.getId)).actionGet.getSource)

    Thread.sleep(1000)


    val searchRequest = new SearchRequest(index)
    val searchSourceBuilder = new SearchSourceBuilder()
    searchSourceBuilder.query(QueryBuilders.matchAllQuery())
    //    searchSourceBuilder.query(QueryBuilders.matchQuery("tags.page_id", "2"))
    searchSourceBuilder.size(10)
    searchSourceBuilder.from(0)
    println(searchSourceBuilder)
    searchRequest.source(searchSourceBuilder)
    val resp = client.search(searchRequest).get()
    println(resp)

    resp.getHits.iterator().asScala.map(f => {
      getMetric2(
        f.getSourceAsMap.get("metric_key").asInstanceOf[String],
        f.getSourceAsMap.get("event_time").asInstanceOf[Long],
        "page_id",
        f.getSourceAsMap.get("tags").asInstanceOf[JMap[String, String]].get("page_id"),
        f.getSourceAsMap.get("value").asInstanceOf[Double]
      )
    }).foreach(println)


    elasticsearchResource.stop()
  }

  private def getJson(cnt: Double, pageId: String = "1", siteId: String = "2"): JMap[String, Any] = {
    val json = new JMap[String, Any]
    val expr = new JMap[String, Double]
    expr.put("cnt", cnt)

    val tags = new JMap[String, String]
    tags.put("page_id", pageId)
    tags.put("site_id", siteId)

    json.put("metric_key", "a")
    json.put("event_time", System.currentTimeMillis())
    json.put("tags", tags)
    json.put("expr", expr)
    json.put("value", cnt)
    json
  }

  private def getMetric2(metricKey: String, t: Long, tagK: String, tagV: String, v: Double): TdqMetric = {
    new TdqMetric(metricKey, t)
      .putTag(tagK, tagV)
      .genUID
      .setValue(v)
  }

}
