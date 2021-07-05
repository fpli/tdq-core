package com.ebay.tdq.expressions.analysis


import com.ebay.tdq.expressions._
import com.ebay.tdq.types._

import scala.util.Try

/**
 * @see ImplicitTypeCasts
 * @author juntzhang
 */
object TypeCoercionRule {

  def copy(opt: BinaryOperator, name: String, value: Expression): BinaryOperator = {
    val clazz = opt.getClass
    Try(opt.getClass.getDeclaredField(name)).toOption match {
      case Some(field) =>
        field.setAccessible(true)
        clazz.getMethod(name).invoke(opt)
        field.set(opt, value)
      case _ =>
    }
    opt
  }

  val numericPrecedence =
    IndexedSeq(
      ByteType,
      ShortType,
      IntegerType,
      LongType,
      FloatType,
      DoubleType)

  val findTightestCommonType: (DataType, DataType) => Option[DataType] = {
    case (t1, t2) if t1 == t2 => Some(t1)
    case (NullType, t1) => Some(t1)
    case (t1, NullType) => Some(t1)

    case (t1: IntegralType, t2: DecimalType) if t2.isWiderThan(t1) =>
      Some(t2)
    case (t1: DecimalType, t2: IntegralType) if t1.isWiderThan(t2) =>
      Some(t1)

    // Promote numeric types to the highest of the two
    case (t1: NumericType, t2: NumericType)
      if !t1.isInstanceOf[DecimalType] && !t2.isInstanceOf[DecimalType] =>
      val index = numericPrecedence.lastIndexWhere(t => t == t1 || t == t2)
      Some(numericPrecedence(index))

    case (_: TimestampType, _: DateType) | (_: DateType, _: TimestampType) =>
      Some(TimestampType)

    case _ => None
  }

  def coerceTypes(expr: Expression): Expression = {
    expr match {
      case b@BinaryOperator(left, right) =>
        (left, right) match {
          case (l: Literal, r) if r.dataType.isInstanceOf[DecimalType] && l.dataType.isInstanceOf[IntegralType] =>
            copy(b, "left", Cast(l, DecimalType.fromLiteral(l), forPrecision = true))
          case (l, r: Literal) if l.dataType.isInstanceOf[DecimalType] && r.dataType.isInstanceOf[IntegralType] =>
            copy(b, "right", Cast(r, DecimalType.fromLiteral(r), forPrecision = true))
          // Promote integers inside a binary expression with fixed-precision decimals to decimals,
          // and fixed-precision decimals in an expression with floats / doubles to doubles
          case (l@IntegralType(), r@DecimalType.Expression(_, _)) =>
            copy(b, "left", Cast(l, DecimalType.forType(l.dataType), forPrecision = true))
          case (DecimalType.Expression(_, _), r@IntegralType()) =>
            copy(b, "right", Cast(r, DecimalType.forType(r.dataType), forPrecision = true))
          case _ =>
            findTightestCommonType(left.dataType, right.dataType).map { commonType =>
              if (b.inputType.acceptsType(commonType)) {
                // If the expression accepts the tightest common type, cast to that.
                if (left.dataType != commonType) {
                  copy(b, "left", Cast(left, commonType))
                }
                if (right.dataType != commonType) {
                  copy(b, "right", Cast(right, commonType))
                }
              }
              b
            }.getOrElse(b) // If there is no applicable conversion, leave expression unchanged.
        }
      case o =>
        o
    }
  }

}

