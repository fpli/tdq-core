package com.ebay.tdq.expressions.aggregate

import com.ebay.tdq.expressions._

/**
 * @author juntzhang
 */
case class Count(child: Expression, cacheKey: Option[String] = None) extends NumberAggregate {
  override protected def eval(input: InternalRow): Any = {
    val v = child.call(input)
    if (v != null) v
    else 1L
  }
}
