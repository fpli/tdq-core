package com.ebay.tdq.expressions

import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
object TypeCoercion {
  // See https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Types.
  // The conversion for integral and floating point types have a linear widening hierarchy:
  val numericPrecedence =
  IndexedSeq(
    ByteType,
    ShortType,
    IntegerType,
    LongType,
    FloatType,
    DoubleType)
}
