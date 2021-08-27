package com.ebay.tdq.expressions

import com.ebay.tdq.expressions.TypeCheckResult._
import com.ebay.tdq.types._

/**
 * @author juntzhang
 */

abstract class RoundBase(child: Expression, scale: Expression,
  mode: BigDecimal.RoundingMode.Value, modeStr: String)
  extends BinaryExpression with Serializable with ImplicitCastInputTypes {

  override def left: Expression = child

  override def right: Expression = scale

  // round of Decimal would eval to null if it fails to `changePrecision`
  override def nullable: Boolean = true

  override def foldable: Boolean = child.foldable

  override lazy val dataType: DataType = child.dataType match {
    // if the new scale is bigger which means we are scaling up,
    // keep the original scale as `Decimal` does
    case DecimalType.Fixed(p, s) => DecimalType(p, if (_scale > s) s else _scale)
    case t => t
  }

  override def inputTypes: Seq[AbstractDataType] = Seq(NumericType, IntegerType)

  override def checkInputDataTypes(): TypeCheckResult = {
    super.checkInputDataTypes() match {
      case TypeCheckSuccess =>
        if (scale.foldable) {
          TypeCheckSuccess
        } else {
          TypeCheckFailure("Only foldable Expression is allowed for scale arguments")
        }
      case f => f
    }
  }
  val EmptyRow: InternalRow = null
  // Avoid repeated evaluation since `scale` is a constant int,
  // avoid unnecessary `child` evaluation in both codegen and non-codegen eval
  // by checking if scaleV == null as well.
  private lazy val scaleV: Any = scale.call(EmptyRow)
  private lazy val _scale: Int = scaleV.asInstanceOf[Int]

  override def eval(input: InternalRow): Any = {
    if (scaleV == null) { // if scale is null, no need to eval its child at all
      null
    } else {
      val evalE = child.call(input)
      if (evalE == null) {
        null
      } else {
        nullSafeEval(evalE)
      }
    }
  }

  // not overriding since _scale is a constant int at runtime
  def nullSafeEval(input1: Any): Any = {
    dataType match {
      case DecimalType.Fixed(_, s) =>
        val decimal = input1.asInstanceOf[Decimal]
        decimal.toPrecision(decimal.precision, s, mode)
      case ByteType =>
        BigDecimal(input1.asInstanceOf[Byte]).setScale(_scale, mode).toByte
      case ShortType =>
        BigDecimal(input1.asInstanceOf[Short]).setScale(_scale, mode).toShort
      case IntegerType =>
        BigDecimal(input1.asInstanceOf[Int]).setScale(_scale, mode).toInt
      case LongType =>
        BigDecimal(input1.asInstanceOf[Long]).setScale(_scale, mode).toLong
      case FloatType =>
        val f = input1.asInstanceOf[Float]
        if (f.isNaN || f.isInfinite) {
          f
        } else {
          BigDecimal(f.toDouble).setScale(_scale, mode).toFloat
        }
      case DoubleType =>
        val d = input1.asInstanceOf[Double]
        if (d.isNaN || d.isInfinite) {
          d
        } else {
          BigDecimal(d).setScale(_scale, mode).toDouble
        }
    }
  }
}


case class Round(child: Expression, scale: Expression, cacheKey: Option[String])
  extends RoundBase(child, scale, BigDecimal.RoundingMode.HALF_UP, "ROUND_HALF_UP")
    with Serializable with ImplicitCastInputTypes {
  def this(child: Expression) = this(child, Literal(0), None)
}