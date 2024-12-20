package com.ebay.tdq

import com.ebay.tdq.common.model.InternalMetric
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test

/**
 * @author juntzhang
 */
class TdqMetricTest {
  @Test
  def test_toSeconds(): Unit = {
    val m = new InternalMetric(
      "a", DateUtils.parseDate("2021-05-06 12:05:00", "yyyy-MM-dd HH:mm:ss").getTime
    )
      .putTag("a1", "1")
      .putTag("a2", "2")
      .putExpr("b1", 1d)
      .putExpr("b2", 2d)
      .genMetricId()
      .setValue(2d)
    val m2 = new InternalMetric(
      "a", DateUtils.parseDate("2021-05-06 12:05:00", "yyyy-MM-dd HH:mm:ss").getTime
    )
      .putTag("a1", "1")
      .putTag("a2", "2")
      .putExpr("b1", 11d)
      .putExpr("b2", 22d)
      .genMetricId()
      .setValue(22d)

    assert(m.getMetricIdWithEventTime == m2.getMetricIdWithEventTime)
  }
}
