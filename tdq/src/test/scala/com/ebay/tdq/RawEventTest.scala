package com.ebay.tdq

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.sojourner.common.util.SojTimestamp
import org.apache.commons.lang.time.DateUtils

import scala.util.Random

/**
 * @author juntzhang
 */
object RawEventTest {
  def getRawEvent(eventTimestamp: String,
    contentLength: Double = 29d, pageId: Int = 0,
    tDuration: Double = 0d, itm: String = "", siteId: String = ""
  ): RawEvent = {
    val e = getRawEvent(DateUtils.parseDate(eventTimestamp, Array("yyyy-MM-dd HH:mm:ss")).getTime)
    e.getClientData.setContentLength(contentLength.toString)
    e.getSojA.put("p", pageId.toString)
    e.getSojA.put("itm", itm)
    e.getSojA.put("siteId", siteId)
    e.getSojA.put("TDuration", tDuration.toString)
    e
  }

  def getRawEvent(eventTimestamp: Long): RawEvent = {
    val siteId = String.valueOf(getSiteId)
    val item = getItm
    val tDuration = String.valueOf(getInt % 100)
    val pageId = Seq[String]("711", "1702898", "1677718")(getInt % 2)
    val contentLength = String.valueOf(getInt % 100)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
    rawEvent.setEventTimestamp(SojTimestamp.getSojTimestamp(eventTimestamp))
    rawEvent.setSojA(new java.util.HashMap[String, String])
    rawEvent.setSojK(new java.util.HashMap[String, String])
    rawEvent.setSojC(new java.util.HashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", tDuration)
    rawEvent.getSojA.put("itm", item)
    rawEvent
  }

  def getRawEvent(): RawEvent = {
    val siteId = String.valueOf(getSiteId)
    val item = getItm
    val tDuration = String.valueOf(getInt % 100)
    val pageId = Seq[String]("711", "1702898", "1677718")(getInt % 2)
    val contentLength = String.valueOf(getInt % 100)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
    rawEvent.setEventTimestamp(SojTimestamp.getSojTimestamp(System.currentTimeMillis))
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new java.util.HashMap[String, String])
    rawEvent.setSojK(new java.util.HashMap[String, String])
    rawEvent.setSojC(new java.util.HashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", tDuration)
    rawEvent.getSojA.put("itm", item)
    rawEvent
  }

  def getItm: String = Seq[String]("123", "1abc", "", null)(getInt % 4)

  def getInt: Int = Math.abs(new Random().nextInt)

  def getLong: Long = Math.abs(new Random().nextLong())

  def getSiteId: Int = Seq[Int](1, 2, 3, 4)(getInt % 1)

}
