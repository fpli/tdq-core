package com.ebay.tdq

import com.ebay.tdq.types.Decimal
import org.scalatest.FunSuite

/**
 * @author juntzhang
 */
class DecimalTest extends FunSuite {
  test("Decimal") {
    assert(Decimal(BigDecimal(11, 0), 6, 1) == Decimal(11, 12, 0))
  }
}
