package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.{ProfilingSqlParser, TdqMetric}
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.Test


class ExpressionTest {
  def getTdqConfig(expr: String): TdqConfig = {
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
         |          "transformations": [
                      {"alias": "p1", "expr": "sum($expr)"}
         |          ]
         |        }
         |      ]
         |    }
         |  ]
         |}
         |""".stripMargin
    JsonUtils.parseObject(json, classOf[TdqConfig])
  }

  def test(expr: String, soj: JHashMap[String, String], assertFunction: TdqMetric => Unit, eventTime: Long = 3829847994095000L): Unit = {
    val config = getTdqConfig(expr)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    val plan = parser.parsePlan()
    println(plan)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength("55")
    rawEvent.setEventTimestamp(eventTime)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.putAll(soj)

    val metric = plan.process(rawEvent)
    assert(metric != null)
    assertFunction.apply(metric)
  }

  @Test
  def test_not_rlike(): Unit = {
    val soj = new JHashMap[String, String]
    test("case when soj_nvl('u') not similar to '\\\\d+' then 1 else 0 end", soj, metric => {
      assert(metric.getValues.get("p1") == 0)
    })
  }

  @Test
  def test_rlike(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("u", "11111")
    test("case when soj_nvl('u') similar to '\\\\d+' then 1 else 0 end", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })
  }

  @Test
  def test_like(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("u", "12")
    test("case when soj_nvl('u') like '1%' then 1 else 0 end", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })
    test("case when soj_nvl('u') not like '2%' then 1 else 0 end", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })
  }
}
