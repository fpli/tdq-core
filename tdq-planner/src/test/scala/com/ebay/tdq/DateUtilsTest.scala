package com.ebay.tdq

import com.ebay.sojourner.common.util.SojTimestamp
import com.ebay.tdq.utils.DateUtils._
import org.apache.commons.lang3.time.{DateFormatUtils, FastDateFormat}
import org.junit.Test

/**
 * @author juntzhang
 */
class DateUtilsTest {
  @Test
  def test_getMinBuckets(): Unit = {
    println(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-08-04 16:55:00").getTime)
    println(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-07-29 14:00:00").getTime)
    println(FastDateFormat.getInstance("yyyyMMddHHmm").format(1621391339604L))
    assert(getMinBuckets(1621391339604L, 10) == "202105191020")
    assert(getMinBuckets(1621391339604L, 5) == "202105191025")
    println(SojTimestamp.getSojTimestampToUnixTimestamp(3835843200089000L))
  }

  @Test
  def test_toSeconds(): Unit = {
    assert(toSeconds("1day") == 86400L)
    assert(toSeconds("1d") == 86400L)
    assert(toSeconds("10min") == 600L)
    assert(toSeconds("1h") == 3600L)


    //    println(org.apache.commons.lang3.time.DateUtils.parseDate("292269055-12-02 09:47:04", "yyyy-MM-dd HH:mm:ss").getTime)
    //    println(DateFormatUtils.format(SojTimestamp.getSojTimestamp¬(9223058401465624L), "yyyy-MM-dd HH:mm:ss"))
    println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"))
    println(DateFormatUtils.format((System.currentTimeMillis() / 1000 / 60 * 60 + 59) * 1000, "yyyy-MM-dd HH:mm:ss"))
  }
}