package com.ebay.tdq

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
class TdqEventMapTypeTest {
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

  def getSchema(s: String): Schema = {
    new Schema.Parser().parse(s)
  }

  def test(expr1: String, expr2: String, schema: String, createEvent: () => TdqEvent, assertFunction: TdqMetric => Unit): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new TdqEnv(),
      schema = getSchema(schema)
    )
    val plan = parser.parsePlan()
    println(plan)
    val metric = plan.process(createEvent())
    assert(metric != null)
    assertFunction.apply(metric)
  }

  /**
   * tdq field support payload['annotation.nId']
   */
  @Test
  def test_map1(): Unit = {
    test("case when p2='123' then 1 else 0 end", "payload.annotation",
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin, () => {
        val tdqEvent = new TdqEvent(
          Map(
            "event_timestamp" -> System.currentTimeMillis(),
            "payload" -> Map(
              "annotation" -> "123"
            ).asJava
          ).mapValues(_.asInstanceOf[Object]).asJava,
          "event_timestamp"
        )
        tdqEvent
      }, metric => {
        println(metric)
        Assert.assertEquals(1d, metric.getValues.get("p1"))
      })
  }

  @Test
  def test_map2(): Unit = {
    test("case when p2='123' then 1 else 0 end", "payload['annotation.nId']",
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin, () => {
        val tdqEvent = new TdqEvent(
          Map(
            "event_timestamp" -> System.currentTimeMillis(),
            "payload" -> Map(
              "annotation.nId" -> "123"
            ).asJava
          ).mapValues(_.asInstanceOf[Object]).asJava,
          "event_timestamp"
        )
        tdqEvent
      }, metric => {
        println(metric)
        Assert.assertEquals(1d, metric.getValues.get("p1"))
      })
  }

  def test_map_map(): Unit = {
    test("case when p2='123' then 1 else 0 end", "payload['annotation.nId']['a.b']",
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "[ "null", { "type": "map", "values": "string" }]" }]       }
        |  ]
        |}
        |""".stripMargin, () => {
        val tdqEvent = new TdqEvent(
          Map(
            "event_timestamp" -> System.currentTimeMillis(),
            "payload" -> Map(
              "annotation.nId" -> Map(
                "a.b" -> "123"
              ).asJava
            ).asJava
          ).mapValues(_.asInstanceOf[Object]).asJava,
          "event_timestamp"
        )
        tdqEvent
      }, metric => {
        println(metric)
        Assert.assertEquals(1d, metric.getValues.get("p1"))
      })
  }
}
