package com.ebay.tdq

import com.ebay.sojourner.common.model.{ClientData, RawEvent}

import scala.util.Random

/**
 * @author juntzhang
 */
object RawEventTest {
  def getRawEvent(): RawEvent = {
    val siteId = String.valueOf(getSiteId)
    val item = getItm
    val tDuration = String.valueOf(getInt % 100)
    val pageId = Seq[String]("711", "1702898", "1677718")(getInt % 2)
    val contentLength = String.valueOf(getInt % 100)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
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
