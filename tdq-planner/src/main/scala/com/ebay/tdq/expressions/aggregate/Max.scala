package com.ebay.tdq.expressions.aggregate

import com.ebay.tdq.expressions._
import com.ebay.tdq.types.TypeUtils

/**
 * @author juntzhang
 */
case class Max(child: Expression, cacheKey: Option[String] = None) extends NumberAggregate {
  override def merge(input1: Any, input2: Any): Any = {
    TypeUtils.getNumeric(dataType).max(input1, input2)
  }
}