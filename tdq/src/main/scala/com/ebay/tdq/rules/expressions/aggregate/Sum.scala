package com.ebay.tdq.rules.expressions.aggregate

import java.util

import com.ebay.tdq.rules.TdqMetric
import com.ebay.tdq.rules.expressions._

/**
 * @author juntzhang
 */
abstract class NumberAggregate extends AggregateExpression {
  def child: Expression

  override def dataType: DataType = resultType

  override def children: Seq[Expression] = Seq(child)

  override def nullable: Boolean = true

  private lazy val zero = Cast(Literal(0), resultType)

  override protected def eval(input: InternalRow, fromCache: Boolean): Any = {
    child.call(input, fromCache)
  }

  lazy val resultType: NumericType = child.dataType match {
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision + 10, scale)
    case _: IntegralType => LongType
    case _ => DoubleType
  }

  override def merge(input1: TdqMetric, input2: TdqMetric): TdqMetric = {
    val keys = new util.HashSet[String](input1.getExprMap.keySet)
    val output = input1.copy
    if (output.getEventTime < input2.getEventTime) {
      output.setEventTime(input2.getEventTime)
    }
    keys.addAll(input2.getExprMap.keySet)
    import scala.collection.JavaConversions._
    for (k <- keys) {
      val v = TypeUtils.getNumeric(dataType).plus(
        input1.getExprMap.get(k),
        input2.getExprMap.get(k)
      )
      output.getExprMap.put(k, v.asInstanceOf[Number])
    }
    output
  }
}

case class Sum(child: Expression, cacheKey: Option[String]) extends NumberAggregate