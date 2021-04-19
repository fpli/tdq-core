package com.ebay.tdq.rules.expressions

/**
 * @author juntzhang
 */
object Utils {
  /**
   * NaN-safe version of `java.lang.Double.compare()` which allows NaN values to be compared
   * according to semantics where NaN == NaN and NaN is greater than any non-NaN double.
   */
  def nanSafeCompareDoubles(x: Double, y: Double): Int = {
    val xIsNan: Boolean = java.lang.Double.isNaN(x)
    val yIsNan: Boolean = java.lang.Double.isNaN(y)
    if ((xIsNan && yIsNan) || (x == y)) 0
    else if (xIsNan) 1
    else if (yIsNan) -1
    else if (x > y) 1
    else -1
  }

  def nanSafeCompareFloats(x: Float, y: Float): Int = {
    val xIsNan: Boolean = java.lang.Float.isNaN(x)
    val yIsNan: Boolean = java.lang.Float.isNaN(y)
    if ((xIsNan && yIsNan) || (x == y)) 0
    else if (xIsNan) 1
    else if (yIsNan) -1
    else if (x > y) 1
    else -1
  }
}
