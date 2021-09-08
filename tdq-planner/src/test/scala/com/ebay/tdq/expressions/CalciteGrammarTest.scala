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
      |    { "name": "payload",             "type": [ "null", { "type": "map", "values": "string" }]       },
      |    { "name": "array1",              "type": [ "null", { "type": "array", "items": "string" }]       },
      |    { "name": "applicationPayload",  "type": [ "null", { "type": "map", "values": "string" }]       }
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
        "array1" -> Array("a", "b", "c"),
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
    test1(s"case when p2='b' then 1 else 0 end", "element_at(array1, 2)", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })

    test1(s"case when p2='2' then 1 else 0 end", "element_at(applicationPayload, 'element_at_test')", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })
  }

  @Test
  def test_split(): Unit = {
    test1(s"case when p2='two' then 1 else 0 end", "element_at(split('oneAtwoBthreeC', '[ABC]'), 2)", metric => {
      println(metric)
      assert(1d == metric.getValues.get("p1"))
    })

    test1(s"case when p2='iOS' then 1 else 0 end", "element_at(split('Script=/v1/batchtrack&Agent=ebayUserAgent/eBayIOS;6.24.0;iOS;14.4;Apple;iPhone11_8;vodafone UK;414x896;2.0&Server=apisd.ebay.com&corrId=04524f74ce342c26&TType=URL&TPayload=corr_id_%3D04524f74ce342c26%26node_id%3Ddb588e93455a484c%26REQUEST_GUID%3D17b4f66a-2750-a44c-7384-74d8e13305f1%26logid%3Dt6faabwwmtuf%253C%253Dpiebgbcsqnuq%2560%2528k%253Emam%2Aw%2560ut3527-17b4f66a26a-0x2354&TStamp=07:38:51.49&TPool=r1edgetrksvc&TDuration=13&ContentLength=1174&TName=Ginger.v1.batchtrack.POST&nodeId=db588e93455a484c&ForwardedFor=82.33.165.249, 104.80.195.151,23.212.109.46,10.221.13.48,10.196.169.225&TMachine=10.68.199.56', ';'), 3)", metric => {
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
