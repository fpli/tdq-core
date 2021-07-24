package com.ebay.tdq

import com.ebay.tdq.rules.TdqMetric
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test

/**
 * @author juntzhang
 */
class TdqMetricTest {
  @Test
  def test_toSeconds(): Unit = {
    val m = new TdqMetric(
      "a", DateUtils.parseDate("2021-05-06 12:05:00", "yyyy-MM-dd HH:mm:ss").getTime
    )
      .putTag("a1", "1")
      .putTag("a2", "2")
      .putExpr("b1", 1d)
      .putExpr("b2", 2d)
      .genUID()
      .setValue(2d)
    val m2 = new TdqMetric(
      "a", DateUtils.parseDate("2021-05-06 12:05:00", "yyyy-MM-dd HH:mm:ss").getTime
    )
      .putTag("a1", "1")
      .putTag("a2", "2")
      .putExpr("b1", 11d)
      .putExpr("b2", 22d)
      .genUID()
      .setValue(22d)

    assert(m.getTagIdWithEventTime == m2.getTagIdWithEventTime)
  }
}
