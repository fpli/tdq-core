package com.ebay.tdq

import com.ebay.tdq.util.DateUtils.toSeconds
import org.junit.Test

/**
 * @author juntzhang
 */
class DateUtilsTest {
  @Test
  def test_toSeconds(): Unit = {
    assert(toSeconds("1day") == 86400L)
    assert(toSeconds("1d") == 86400L)
    assert(toSeconds("10min") == 600L)
    assert(toSeconds("1h") == 3600L)
  }
}
