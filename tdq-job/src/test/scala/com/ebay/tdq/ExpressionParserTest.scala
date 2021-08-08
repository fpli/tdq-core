package com.ebay.tdq

import java.util

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.config.KafkaSourceConfig
import com.ebay.tdq.expressions.InternalRow
import com.ebay.tdq.rules.ExpressionParser
import org.apache.avro.Schema
import org.junit.{Assert, Test}

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
class ExpressionParserTest {
  @Test
  def test_timestamp(): Unit = {
    val ksc = new KafkaSourceConfig()
    ksc.setEventTimeField("eventTimestamp")
    val schema = new Schema.Parser().parse(
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "eventTimestamp",    "type": "long"       },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin)
    val eventTimestamp = 1628307182114L
    val tdqEvent = new TdqEvent(
      Map(
        "eventTimestamp" -> eventTimestamp,
        "payload" -> Map(
          "annotation.nId" -> "123"
        ).asJava
      ).mapValues(_.asInstanceOf[Object]).asJava
    )
    val cacheData = new util.HashMap[String, Any]
    cacheData.put("__TDQ_EVENT", tdqEvent)

    val parser = ExpressionParser(ksc.getEventTimeField, new TdqEnv(), schema)
    val expr = parser.parse()

    tdqEvent.buildEventTime(expr.call(InternalRow.apply(null, cacheData)).asInstanceOf[Long])

    Assert.assertEquals(eventTimestamp, tdqEvent.get("event_time_millis"))
    Assert.assertEquals(eventTimestamp * 1000, tdqEvent.get("event_timestamp"))
  }

  @Test
  def test_map_timestamp_cast_long(): Unit = {
    val ksc = new KafkaSourceConfig()
    ksc.setEventTimeField("cast(payload['timestamp.created'] as LONG)")
    val schema = new Schema.Parser().parse(
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin)
    val timeStr = "1628307182114"
    val tdqEvent = new TdqEvent(
      Map(
        "payload" -> Map(
          "annotation.nId" -> "123",
          "timestamp.created" -> timeStr
        ).asJava
      ).mapValues(_.asInstanceOf[Object]).asJava
    )
    val cacheData = new util.HashMap[String, Any]
    cacheData.put("__TDQ_EVENT", tdqEvent)

    val parser = ExpressionParser(ksc.getEventTimeField, new TdqEnv(), schema)
    val expr = parser.parse()

    val s = System.currentTimeMillis()
    (0 to 10000).foreach { _ =>
      tdqEvent.buildEventTime(expr.call(InternalRow.apply(null, cacheData)).asInstanceOf[Long])
    }
    println(s"cost ${System.currentTimeMillis() - s} ms")

    Assert.assertEquals(timeStr.toLong, tdqEvent.get("event_time_millis"))
    Assert.assertEquals(timeStr.toLong * 1000, tdqEvent.get("event_timestamp"))
  }
}
