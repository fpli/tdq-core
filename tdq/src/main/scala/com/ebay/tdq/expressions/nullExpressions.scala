package com.ebay.tdq.expressions

/**
 * @author juntzhang
 */
case class IsNull(child: Expression) extends UnaryExpression with Predicate {
  override def nullable: Boolean = false

  override def eval(input: InternalRow, fromCache: Boolean): Any = {
    child.call(input, fromCache) == null
  }

  override def sql: String = s"(${child.sql} IS NULL)"
}


case class IsNotNull(child: Expression) extends UnaryExpression with Predicate {
  override def nullable: Boolean = false

  override def eval(input: InternalRow, fromCache: Boolean): Any = {
    child.call(input, fromCache) != null
  }

  override def sql: String = s"(${child.sql} IS NOT NULL)"
}