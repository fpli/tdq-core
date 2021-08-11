package com.ebay.tdq.rules

import com.ebay.tdq.expressions.Expression

/**
 * @author juntzhang
 */
class DelegatingRegistry(underlying: PartialFunction[RegistryContext, Expression])
  extends PartialFunction[RegistryContext, Expression] {
  def apply(t: RegistryContext): Expression = underlying.apply(t)

  def isDefinedAt(t: RegistryContext): Boolean = underlying.isDefinedAt(t)
}
