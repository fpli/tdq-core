package com.ebay.tdq.expressions

import com.ebay.tdq.types.{AbstractDataType, DataType, DoubleType, FloatType, TypeCollection, TypeUtils}

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

case class Coalesce(children: Seq[Expression], cacheKey: Option[String]) extends Expression {

  /** Coalesce is nullable if all of its children are nullable, or if it has no children. */
  override def nullable: Boolean = children.forall(_.nullable)

  override def checkInputDataTypes(): TypeCheckResult = {
    if (children.length < 1) {
      TypeCheckResult.TypeCheckFailure(
        s"input to function $prettyName requires at least one argument")
    } else {
      TypeUtils.checkForSameTypeInputExpr(children.map(_.dataType), s"function $prettyName")
    }
  }

  override def dataType: DataType = children.head.dataType

  override def eval(input: InternalRow, fromCache: Boolean): Any = {
    var result: Any = null
    val childIterator = children.iterator
    while (childIterator.hasNext && result == null) {
      result = childIterator.next().call(input, fromCache)
    }
    result
  }
}

case class IsNaN(child: Expression) extends UnaryExpression
  with Predicate with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] = Seq(TypeCollection(DoubleType, FloatType))

  override def nullable: Boolean = false

  override def eval(input: InternalRow, fromCache: Boolean): Any = {
    val value = child.call(input, fromCache)
    if (value == null) {
      false
    } else {
      child.dataType match {
        case DoubleType => value.asInstanceOf[Double].isNaN
        case FloatType => value.asInstanceOf[Float].isNaN
      }
    }
  }
}