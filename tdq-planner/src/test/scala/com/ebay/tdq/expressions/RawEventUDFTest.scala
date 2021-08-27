package com.ebay.tdq.expressions

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{TdqEvent, InternalMetric}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.planner.LkpManagerTest
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.{BeforeClass, Test}

object RawEventUDFTest {
  @BeforeClass
  def setup(): Unit = {
    LkpManagerTest.init()
  }
}

class RawEventUDFTest {
  @Test
  def test_soj_nvl(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("udid", "1")
    test("case when length(p2)>0 then 1 else 0 end", "soj_nvl('udid')", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })

    test("case when length(p2)>0 then 1 else 0 end", "soj_nvl('udid_test')", soj, metric => {
      assert(metric.getValues.get("p1") == 0)
    })
  }

  @Test
  def test_is_bbwoa_page(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("p", "2545343")
    test("case when IS_BBWOA_PAGE_WITH_ITM(p2) then 1 else 0 end", "cast(soj_nvl('p') AS INTEGER)", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })

    soj.put("p", "111111111")
    test("case when IS_BBWOA_PAGE_WITH_ITM(p2) then 1 else 0 end", "cast(soj_nvl('p') AS INTEGER)", soj, metric => {
      assert(metric.getValues.get("p1") == 0)
    })
  }

  def test(expr1: String, expr2: String, soj: JHashMap[String, String], assertFunction: InternalMetric => Unit, eventTime: Long = 3829847994095000L): Unit = {
    test0(expr1, expr2, soj, assertFunction, _ => {}, eventTime)
  }

  def test0(expr1: String, expr2: String, soj: JHashMap[String, String], assertFunction: InternalMetric => Unit, rawEventFunction: RawEvent => Unit, eventTime: Long = 3829847994095000L): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(config.getRules.get(0).getProfilers.get(0), window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString), new TdqEnv(), null)
    val plan = parser.parsePlan()
    println(plan)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength("55")
    rawEvent.getClientData.setRemoteIP("100.10.1.1")
    rawEvent.setEventTimestamp(eventTime)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.putAll(soj)
    rawEventFunction(rawEvent)

    val metric = plan.process(new TdqEvent(rawEvent))
    assert(metric != null)
    assertFunction.apply(metric)
  }

  def getTdqConfig(expr1: String, expr2: String): TdqConfig = {
    val json =
      s"""
         |{
         |  "id": "1",
         |  "rules": [
         |    {
         |      "name": "rule_1",
         |      "type": "realtime.rheos.profiler",
         |      "config": {
         |        "window": "1min"
         |      },
         |      "profilers": [
         |        {
         |          "metric-name": "test1",
         |          "expr": "p1",
         |          "dimensions": ["p2"],
         |          "transformations": [
                      {"alias": "p1", "expr": "sum($expr1)"},
         |            {"alias": "p2", "expr": "$expr2"}
         |          ]
         |        }
         |      ]
         |    }
         |  ]
         |}
         |""".stripMargin
    JsonUtils.parseObject(json, classOf[TdqConfig])
  }

  @Test
  def test_ebay_ip(): Unit = {
    val soj = new JHashMap[String, String]
    test0("case when clientData.remoteIP like '10.%' then 1 else 0 end", "clientData.remoteIP", soj, metric => {
      println(metric)
    }, rawEvent => {
      rawEvent.getClientData.setRemoteIP("10.10.1.1")
    })
  }

  @Test
  def test_timestamp(): Unit = {
    val soj = new JHashMap[String, String]
    test(s"case when p2 is not null then 1 else 0 end", "unix_timestamp(event_timestamp)", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
    })

    test(s"case when p2 is not null then 1 else 0 end", "unix_timestamp(event_timestamp)", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
    })
  }

  @Test
  def test_soj_parse_rlogid(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("eprlogid", "t6ulcpjqcj9%253Fjqpsobtlrbn%2528q%257Fr3k*w%2560ut351%253E-179643d1077-0x177")
    test(s"case when p2=0 then 0 else 1 end", "unix_timestamp(event_timestamp) - unix_timestamp(to_timestamp(soj_parse_rlogid(soj_nvl('eprlogid'), 'timestamp')))", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 0)
    })

    test(s"case when p2=0 then 0 else 1 end", "unix_timestamp(event_timestamp) - unix_timestamp(to_timestamp(soj_parse_rlogid(soj_nvl('eprlogid'), 'timestamp')))", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
      assert(metric.getTags.get("p2") == 5)
    }, eventTime = 3829847994095000L + 5000000)
  }
}