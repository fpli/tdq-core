package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{InternalMetric, TdqEvent}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.apache.avro.Schema
import org.junit.{Assert, Test}

import scala.collection.JavaConverters._


class CalciteGrammarTest {
  val eventTime: Long = System.currentTimeMillis()
  val schema: Schema = new Schema.Parser().parse(
    """
      |{
      |  "type": "record",
      |  "name": "TdqEvent",
      |  "namespace": "com.ebay.tdq.common.model",
      |  "fields": [
      |    { "name": "event_timestamp",   "type": "long"                                                 },
      |    { "name": "rdt",   "type": "int"                                                 },
      |    { "name": "urlQueryString",   "type": "string"                                                 },
      |    { "name": "webServer",   "type": "string"                                                 },
      |    { "name": "page_id",   "type": "int"                                                 },
      |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       },
      |    { "name": "applicationPayload",           "type": [ "null", { "type": "map", "values": "string" }]       }
      |  ]
      |}
      |""".stripMargin)

  def test(expr1: String, expr2: String, createEvent: () => TdqEvent, assertFunction: InternalMetric => Unit): Unit = {
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

  def test1(expr1: String, expr2: String, assertFunction: InternalMetric => Unit): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new TdqEnv(),
      schema = schema
    )
    val plan = parser.parsePlan()
    println(plan)
    val tdqEvent = new TdqEvent(
      Map(
        "event_timestamp" -> eventTime,
        "page_id" -> 123,
        "payload" -> Map(
          "tEsT1" -> "123"
        ).asJava,
        "applicationPayload" -> Map(
          "tEsT1" -> "123",
          "element_at_test" -> "2"
        ).asJava
      ).mapValues(_.asInstanceOf[Object]).asJava
    )
    tdqEvent.buildEventTime(eventTime)
    val metric = plan.process(tdqEvent)
    assert(metric != null)
    assertFunction.apply(metric)
  }

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

  @Test
  def test_not_rlike(): Unit = {
    test(s"case when p2 not similar to '\\\\d+' then 1 else 0 end", "'1111'", () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(0d, metric.getValues.get("p1"))
    })
  }

  @Test
  def test_rlike(): Unit = {
    test(s"case when p2 similar to '\\\\d+' then 1 else 0 end", "'1111'", () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
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

  @Test
  def test_like(): Unit = {
    test1("case when p2 like '1%' then 1 else 0 end", "'12'", metric => {
      assert(metric.getValues.get("p1") == 1)
    })

    test1("case when p2 not like '2%' then 1 else 0 end", "'12'", metric => {
      assert(metric.getValues.get("p1") == 1)
    })
  }


  @Test
  def test_round(): Unit = {
    test1(s"case when p2>23 then 1 else 0 end", "round(123213 / 1000,1) - 100", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_concat(): Unit = {
    test1(s"case when p2='ab' then 1 else 0 end", "concat('a','b')", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_element_at(): Unit = {
    test1(s"case when p2='two' then 1 else 0 end", "element_at(split('oneAtwoBthreeC', '[ABC]'), 2)", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
    test1(s"case when p2='2' then 1 else 0 end", "element_at(applicationPayload, 'element_at_test')", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_trim(): Unit = {
    test1(s"case when p2='a' then 1 else 0 end", "trim('  a  ')", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_substr(): Unit = {
    test1(s"case when p2='bc' then 1 else 0 end", "substr('abcd',2, 2)", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
    test1(s"case when p2='cd' then 1 else 0 end", "Substring('abcd',-2)", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_substr2(): Unit = {
    test1(s"case when ((page_id in (4634, 2545226, 2056805, 2046732, 3418065, 3658866) or (page_id = 2499619 and upper(applicationPayload['eactn']) = 'EXPC') or (page_id = 2351460 AND soj_url_decode_escapes(lower(applicationPayload['gf']), '%') like '%seller:specific%' ) ) and rdt=0) then 1 else 0 end", "substr('abcd',2, 2)", metric => {
      println(metric)
    })
  }
}
