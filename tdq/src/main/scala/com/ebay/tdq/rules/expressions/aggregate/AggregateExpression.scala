package com.ebay.tdq.rules.expressions.aggregate

import com.ebay.tdq.rules.TdqMetric
import com.ebay.tdq.rules.expressions.Expression

/**
 * @author juntzhang
 */
abstract class AggregateExpression extends Expression {
  def merge(input1: TdqMetric, input2: TdqMetric): TdqMetric
}
