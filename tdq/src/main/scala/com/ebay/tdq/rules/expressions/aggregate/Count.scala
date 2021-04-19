package com.ebay.tdq.rules.expressions.aggregate

import com.ebay.tdq.rules.expressions._

/**
 * @author juntzhang
 */
case class Count(child: Expression, cacheKey: Option[String] = None) extends NumberAggregate {
  override def dataType: DataType = LongType

  override protected def eval(input: InternalRow, fromCache: Boolean): Any = {
//    val v = call(input, fromCache)
//    if (v != null) return v
    1L
  }
}
