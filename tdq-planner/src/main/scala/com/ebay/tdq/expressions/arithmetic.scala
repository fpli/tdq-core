package com.ebay.tdq.expressions

import com.ebay.tdq.types._
import com.ebay.tdq.utils.CalendarInterval

/**
 * @author juntzhang
 */
abstract class BinaryArithmetic extends BinaryOperator with NullIntolerant {

  override def dataType: DataType = left.dataType

  /** Name of the function for this expression on a [[Decimal]] type. */
  def decimalMethod: String =
    sys.error("BinaryArithmetics must override either decimalMethod or genCode")
}

case class Add(left: Expression, right: Expression, cacheKey: Option[String]) extends BinaryArithmetic {

  private lazy val numeric = TypeUtils.getNumeric(dataType)

  override def inputType: AbstractDataType = TypeCollection.NumericAndInterval

  override def symbol: String = "+"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    if (dataType.isInstanceOf[CalendarIntervalType]) {
      input1.asInstanceOf[CalendarInterval].add(input2.asInstanceOf[CalendarInterval])
    } else {
      numeric.plus(input1, input2)
    }
  }
}

case class Subtract(left: Expression, right: Expression, cacheKey: Option[String]) extends BinaryArithmetic {
  private lazy val numeric = TypeUtils.getNumeric(dataType)

  override def inputType: AbstractDataType = TypeCollection.NumericAndInterval

  override def symbol: String = "-"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    if (dataType.isInstanceOf[CalendarIntervalType]) {
      input1.asInstanceOf[CalendarInterval].subtract(input2.asInstanceOf[CalendarInterval])
    } else {
      numeric.minus(input1, input2)
    }
  }
}

case class Multiply(left: Expression, right: Expression, cacheKey: Option[String]) extends BinaryArithmetic {

  private lazy val numeric = TypeUtils.getNumeric(dataType)

  override def inputType: AbstractDataType = NumericType

  override def symbol: String = "*"

  override def decimalMethod: String = "$times"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = numeric.times(input1, input2)
}

case class Divide(left: Expression, right: Expression, cacheKey: Option[String] = None) extends BinaryArithmetic {

  private lazy val div: (Any, Any) => Any = dataType match {
    case ft: FractionalType => ft.fractional.asInstanceOf[Fractional[Any]].div
  }

  override def inputType: AbstractDataType = TypeCollection(DoubleType, DecimalType)

  override def symbol: String = "/"

  override def decimalMethod: String = "$div"

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val input2 = right.call(input)
    if (input2 == null || input2 == 0) {
      null
    } else {
      val input1 = left.call(input)
      if (input1 == null) {
        null
      } else {
        div(input1, input2)
      }
    }
  }
}

/**
 * Hive only performs integral division with the DIV operator. The arguments to / are always
 * converted to fractional types.
 */
object Division {
  def coerceTypes(expression: Expression): Expression = expression match {
    // Decimal and Double remain the same
    case d: Divide if d.dataType == DoubleType => d
    case d: Divide if d.dataType.isInstanceOf[DecimalType] => d
    case Divide(left, right, cacheKey) if isNumericOrNull(left) && isNumericOrNull(right) =>
      Divide(Cast(left, DoubleType), Cast(right, DoubleType), cacheKey)
  }

  private def isNumericOrNull(ex: Expression): Boolean = {
    // We need to handle null types in case a query contains null literals.
    ex.dataType.isInstanceOf[NumericType] || ex.dataType == NullType
  }
}

case class Remainder(left: Expression, right: Expression, cacheKey: Option[String]) extends BinaryArithmetic {

  private lazy val integral = dataType match {
    case i: IntegralType => i.integral.asInstanceOf[Integral[Any]]
    case i: FractionalType => i.asIntegral.asInstanceOf[Integral[Any]]
  }

  override def inputType: AbstractDataType = NumericType

  override def symbol: String = "%"

  override def decimalMethod: String = "remainder"

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val input2 = right.call(input)
    if (input2 == null || input2 == 0) {
      null
    } else {
      val input1 = left.call(input)
      if (input1 == null) {
        null
      } else {
        input1 match {
          case d: Double => d % input2.asInstanceOf[java.lang.Double]
          case f: Float => f % input2.asInstanceOf[java.lang.Float]
          case _ => integral.rem(input1, input2)
        }
      }
    }
  }
}