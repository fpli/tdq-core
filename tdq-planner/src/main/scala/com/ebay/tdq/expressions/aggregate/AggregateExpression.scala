package com.ebay.tdq.expressions.aggregate

import com.ebay.tdq.expressions.Expression

/**
 * @author juntzhang
 */
abstract class AggregateExpression extends Expression {
  def merge(input1: Any, input2: Any): Any
}