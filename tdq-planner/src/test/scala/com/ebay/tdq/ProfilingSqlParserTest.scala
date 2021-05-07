package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.{PhysicalPlan, ProfilingSqlParser}
import com.ebay.tdq.util.DateUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

/**
 * @author juntzhang
 */
object ProfilingSqlParserTest {
  def getPhysicalPlan(json: String): PhysicalPlan = {
    val objectMapper = new ObjectMapper
    val config: TdqConfig = objectMapper.reader.forType(classOf[TdqConfig]).readValue(json)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString)
    )
    parser.parsePlan()
  }
}

class ProfilingSqlParserTest {
  @Test
  def event_capture_publish_latency(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_1",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "10s"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "event_capture_publish_latency",
        |          "comment": "Event Capture Publish Latency",
        |          "dimensions": ["page_id"],
        |          "expression": {"operator": "Expr", "config": {"text": "t_duration_sum"}},
        |          "filter": "page_id in (1702898, 1677718) and CAST(clientData.contentLength AS double) > 30.0",
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}}
        |            },
        |            {
        |              "alias": "t_duration",
        |              "expression": {
        |                "operator": "Expr",
        |                "config": {"text": "CAST(TAG_EXTRACT('TDuration') AS DOUBLE)"}
        |              }
        |            },
        |            {
        |              "alias": "t_duration_sum",
        |              "expression": {"operator": "SUM", "config": {"arg0": "t_duration"}}
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val objectMapper = new ObjectMapper
    val config: TdqConfig = objectMapper.reader.forType(classOf[TdqConfig]).readValue(json)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString)
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "1"
    val item: String = "123"
    val pageId: String = "1702898"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength("55")
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", "155")
    rawEvent.getSojA.put("itm", item)

    var metric = plan.process(rawEvent)
    assert(metric != null)
    assert(metric.getExprMap.get("t_duration_sum") == 155d)

    rawEvent.getClientData.setContentLength("25")
    metric = plan.process(rawEvent)
    assert(metric == null)

    rawEvent.getClientData.setContentLength("35")
    rawEvent.getSojA.put("TDuration", "10")
    metric = plan.process(rawEvent)
    assert(metric.getExprMap.get("t_duration_sum") == 10d)

  }

  @Test
  def global_mandatory_tag_item_rate(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_3",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "2min"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "global_mandatory_tag_item_rate2",
        |          "comment": "Global Mandatory Tag - Item Rate",
        |          "dimensions": ["page_id"],
        |          "expression": {"operator": "Expr", "config": {"text": "itm_valid_cnt / itm_cnt"}},
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expression": {
        |                "operator": "UDF",
        |                "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}
        |              }
        |            },
        |            {
        |              "alias": "item",
        |              "expression": {
        |                "operator": "UDF",
        |                "config": {"text": "CAST(TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm') AS LONG)"}
        |              }
        |            },
        |            {
        |              "alias": "itm_valid_ind",
        |              "expression": {
        |                "operator": "Expr",
        |                "config": {
        |                  "text": "case when item is not null then 1 else 0 end"
        |                }
        |              }
        |            },
        |            {
        |              "alias": "itm_cnt",
        |              "expression": {"operator": "Count", "config": {"arg0": "1"}}
        |            },
        |            {
        |              "alias": "itm_valid_cnt",
        |              "expression": {
        |                "operator": "Sum", "config": {"arg0": "itm_valid_ind"}
        |              }
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val objectMapper = new ObjectMapper
    val config: TdqConfig = objectMapper.reader.forType(classOf[TdqConfig]).readValue(json)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString)
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "1"
    val item: String = "123"
    val pageId: String = "1702898"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("itm", item)

    var metric = plan.process(rawEvent)
    assert(metric.getExprMap.get("itm_cnt") == 1)
    assert(metric.getExprMap.get("itm_valid_cnt") == 1)

    rawEvent.getSojA.put("itm", "123a")
    metric = plan.process(rawEvent)
    assert(metric.getExprMap.get("itm_cnt") == 1)
    assert(metric.getExprMap.get("itm_valid_cnt") == 0)

    rawEvent.getSojA.remove("itm")
    val s = System.currentTimeMillis()
    val s1 = System.nanoTime()

    (0 to 100000).foreach { _ =>
      rawEvent.getSojA.put("p", Math.abs(RawEventTest.getInt).toString)
      rawEvent.getSojA.put("itm", Math.abs(RawEventTest.getLong).toString)
      metric = plan.process(rawEvent)
    }

    println(s"100k process cast time ${System.currentTimeMillis() - s} ms")
    println(s"100k process cast time ${System.nanoTime() - s1} ns")
    //    assert(metric.getExprMap.get("itm_cnt") == 1)
    //    assert(metric.getExprMap.get("itm_valid_cnt") == 0)

  }
}
