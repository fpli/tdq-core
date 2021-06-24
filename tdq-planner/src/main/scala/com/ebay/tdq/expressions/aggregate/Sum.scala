package com.ebay.tdq.expressions.aggregate

import com.ebay.tdq.expressions._
import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
abstract class NumberAggregate extends AggregateExpression {
  lazy val resultType: NumericType = child.dataType match {
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision + 10, scale)
    case _: IntegralType => LongType
    case _ => DoubleType
  }
  private lazy val zero = Cast(Literal(0), resultType)

  def child: Expression

  override def children: Seq[Expression] = Seq(child)

  override def nullable: Boolean = true

  override def merge(input1: Any, input2: Any): Any = {
    TypeUtils.getNumeric(dataType).plus(input1, input2)
  }

  override def dataType: DataType = resultType

  override protected def eval(input: InternalRow): Any = {
    _child.call(input)
  }

  def _child: Expression = {
    child.dataType match {
      case _: DecimalType => child
      case _: IntegralType => Cast(child, resultType)
      case _ => child
    }
  }
}

case class Sum(child: Expression, cacheKey: Option[String]) extends NumberAggregate