package com.ebay.tdq

import com.ebay.tdq.types.Decimal
import org.junit.Test

/**
 * @author juntzhang
 */
class DecimalTest {
  @Test
  def test_decimal(): Unit = {
    assert(Decimal(BigDecimal(11, 0), 6, 1) == Decimal(11, 12, 0))
  }
}
