package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{TdqEvent, TdqMetric}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.apache.avro.Schema
import org.junit.{Assert, Test}

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
class CaseWhenTest {
  val eventTime: Long = System.currentTimeMillis()

  def getTdqConfig(expr1: String, expr2: String): TdqConfig = {
    val json =
      s"""
         |{
         |  "id": "ignore",
         |  "rules": [
         |    {
         |      "name": "ignore",
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

  def test(expr1: String, expr2: String, schema: Schema, createEvent: () => TdqEvent, assertFunction: TdqMetric => Unit): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new TdqEnv(),
      schema = schema
    )
    val plan = parser.parsePlan()
    println(plan)
    val metric = plan.process(createEvent())
    assert(metric != null)
    assertFunction.apply(metric)
  }

  @Test
  def test_case1(): Unit = {
    val schema = new Schema.Parser().parse(
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "isBot",             "type": ["null", "boolean"]                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin)

    test("case when p2='NULL' then 1 else 0 end", "case when isBot is NULL then 'NULL' when isBot then 'Y' else 'N' end", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
            "annotation.nId" -> "123"
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })

    test("case when p2='Y' then 1 else 0 end", "case when isBot is NULL then 'NULL' when isBot then 'Y' else 'N' end", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "isBot" -> true,
          "event_timestamp" -> eventTime,
          "payload" -> Map(
            "annotation.nId" -> "123"
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })

    test("case when p2='N' then 1 else 0 end", "case when isBot is NULL then 'NULL' when isBot then 'Y' else 'N' end", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "isBot" -> false,
          "event_timestamp" -> eventTime,
          "payload" -> Map(
            "annotation.nId" -> "123"
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })

  }
}
