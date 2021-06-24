package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.{Count, Sum}
import com.ebay.tdq.rules.{AggrPhysicalPlan, PhysicalPlan}
import com.ebay.tdq.types._
import org.junit.Test

/**
 * @author juntzhang
 */
class PhysicalPlanTest {
  @Test
  def event_capture_publish_latency(): Unit = {
    val event = GetRawEvent()
    val page_id = Cast(ExtractTag(event, "p", StringType), IntegerType, Some("page_id"))
    val TDuration = Cast(ExtractTag(event, "TDuration", StringType), DoubleType, Some("t_duration"))
    val t_duration_sum = Sum(TDuration, Some("t_duration_sum"))
    val expr = GetStructField("t_duration_sum", DoubleType)
    val filter = And(
      In(page_id, Seq(Literal(1702898, IntegerType), Literal(1677718, IntegerType))),
      GreaterThan(Cast(GetStructField("clientData.contentLength", StringType), IntegerType), Literal(30, IntegerType))
    )
    val plan = PhysicalPlan(
      "test", 5000, expr,
      Array(AggrPhysicalPlan(name = "t_duration_sum", evaluation = t_duration_sum)),
      Array(page_id),
      filter
    )

    //val arr = new ByteArrayOutputStream()
    //val stream = new ObjectOutputStream(arr)
    //stream.writeObject(plan)
    //println(arr.size())

    val siteId: String = "1"
    val item: String = "123"
    val pageId: String = "1702898"
    val contentLength: String = "55"
    val tDuration: String = "155"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", tDuration)
    rawEvent.getSojA.put("itm", item)

    val v1 = plan.process(rawEvent)
    println(v1)


    rawEvent.getClientData.setContentLength("25")
    rawEvent.getSojA.put("TDuration", "125")
    val v2 = plan.process(rawEvent)
    assert(v2 == null)

    rawEvent.getClientData.setContentLength("35")
    rawEvent.getSojA.put("TDuration", "135")
    val v3 = plan.process(rawEvent)
    println(v3)

    //    val ans = t_duration_sum.merge(v1, v3)
    //    plan.evaluate(ans)
    //    println(ans)
    //
    //    val s = System.currentTimeMillis()
    //    (0 to 100000).foreach(_ => plan.process(rawEvent))
    //    println(s"100k process cast time ${System.currentTimeMillis() - s} ms")
  }

  @Test
  def global_mandatory_tag_item_rate(): Unit = {
    val eventExpression = GetRawEvent()
    val pageIdExpression = Cast(ExtractTag(eventExpression, "p", StringType), IntegerType, Some("page_id"))
    val itemExpression = ExtractTag(eventExpression, "itm|itmid|itm_id|itmlist|litm", StringType, Some("item"))
    val itmValidIndExpression = Cast(
      CaseWhen(
        Seq((GreaterThan(Length(RegExpExtract(itemExpression, Literal("^(\\d+(%2C)?)+$"))), Literal(0)), Literal(1L))),
        Some(Literal(0L))
      ), DoubleType, Some("itm_valid_ind")
    )

    val itmValidCntExpression = Sum(itmValidIndExpression, Some("itm_valid_cnt"))
    val itmCntExpression = Count(Literal(1L), Some("itm_cnt"))

    val expr = Divide(itmValidCntExpression, itmCntExpression)
    val filter = And(
      In(pageIdExpression, Seq(Literal(1702898, IntegerType), Literal(1677718, IntegerType))),
      GreaterThan(Cast(GetStructField("clientData.contentLength", StringType), IntegerType), Literal(30))
    )
    val plan = PhysicalPlan(
      "test", 5000,
      expr,
      Array(
        AggrPhysicalPlan(name = "itm_valid_cnt", evaluation = itmValidCntExpression),
        AggrPhysicalPlan(name = "itm_cnt", evaluation = itmCntExpression)
      ),
      Array(pageIdExpression),
      filter
    )

    val siteId: String = "1"
    val item: String = "123"
    val pageId: String = "1702898"
    val contentLength: String = "55"
    val tDuration: String = "155"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", tDuration)
    rawEvent.getSojA.put("itm", item)

    val v1 = plan.process(rawEvent)
    println(v1)


    rawEvent.getClientData.setContentLength("25")
    rawEvent.getSojA.put("itm", "123")
    val v2 = plan.process(rawEvent)
    assert(v2 == null)

    rawEvent.getClientData.setContentLength("35")
    rawEvent.getSojA.put("itm", "123a")
    val v3 = plan.process(rawEvent)
    println(v3)

    val ans = plan.merge(v1, v3)
    println(ans)
    //    plan.evaluate(ans)
    //    println(ans)

    val s = System.currentTimeMillis()
    (0 to 100000).foreach(_ => plan.process(rawEvent))
    println(s"100k process cast time ${System.currentTimeMillis() - s} ms")
  }
}
