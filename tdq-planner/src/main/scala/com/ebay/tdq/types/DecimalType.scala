package com.ebay.tdq.types

import java.util.Locale

import com.ebay.tdq.expressions.{Expression, Literal}

import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
case class DecimalType(precision: Int, scale: Int) extends FractionalType {

  if (scale > precision) {
    throw new Exception(
      s"Decimal scale ($scale) cannot be greater than precision ($precision).")
  }

  if (precision > DecimalType.MAX_PRECISION) {
    throw new Exception(s"DecimalType can only support precision up to 38")
  }

  type InternalType = Decimal
  @transient lazy val tag = typeTag[InternalType]
  val numeric = Decimal.DecimalIsFractional
  val fractional = Decimal.DecimalIsFractional
  val ordering = Decimal.DecimalIsFractional
  val asIntegral = Decimal.DecimalAsIfIntegral

  // default constructor for Java
  def this(precision: Int) = this(precision, 0)

  def this() = this(10)

  override def toString: String = s"DecimalType($precision,$scale)"

  override def sql: String = typeName.toUpperCase(Locale.ROOT)

  override def typeName: String = s"decimal($precision,$scale)"

  /**
   * Returns whether this DecimalType is wider than `other`. If yes, it means `other`
   * can be casted into `this` safely without losing any precision or range.
   */
  def isWiderThan(other: DataType): Boolean = other match {
    case dt: DecimalType =>
      (precision - scale) >= (dt.precision - dt.scale) && scale >= dt.scale
    case dt: IntegralType =>
      isWiderThan(DecimalType.forType(dt))
    case _ => false
  }

  /**
   * Returns whether this DecimalType is tighter than `other`. If yes, it means `this`
   * can be casted into `other` safely without losing any precision or range.
   */
  def isTighterThan(other: DataType): Boolean = other match {
    case dt: DecimalType =>
      (precision - scale) <= (dt.precision - dt.scale) && scale <= dt.scale
    case dt: IntegralType =>
      isTighterThan(DecimalType.forType(dt))
    case _ => false
  }

  /**
   * The default size of a value of the DecimalType is 8 bytes when precision is at most 18,
   * and 16 bytes otherwise.
   */
  override def defaultSize: Int = if (precision <= Decimal.MAX_LONG_DIGITS) 8 else 16

  override def simpleString: String = s"decimal($precision,$scale)"

  def asNullable: DecimalType = this
}


object DecimalType extends AbstractDataType {

  import scala.math.min

  val MAX_PRECISION = 38
  val MAX_SCALE = 38
  val SYSTEM_DEFAULT: DecimalType = DecimalType(MAX_PRECISION, 18)
  val USER_DEFAULT: DecimalType = DecimalType(10, 0)
  val MINIMUM_ADJUSTED_SCALE = 6

  // The decimal types compatible with other numeric types
  val ByteDecimal = DecimalType(3, 0)
  val ShortDecimal = DecimalType(5, 0)
  val IntDecimal = DecimalType(10, 0)
  val LongDecimal = DecimalType(20, 0)
  val FloatDecimal = DecimalType(14, 7)
  val DoubleDecimal = DecimalType(30, 15)
  val BigIntDecimal = DecimalType(38, 0)

  def fromLiteral(literal: Literal): DecimalType = literal.value match {
    case v: Short => fromBigDecimal(BigDecimal(v))
    case v: Int => fromBigDecimal(BigDecimal(v))
    case v: Long => fromBigDecimal(BigDecimal(v))
    case _ => forType(literal.dataType)
  }

  def forType(dataType: DataType): DecimalType = dataType match {
    case ByteType => ByteDecimal
    case ShortType => ShortDecimal
    case IntegerType => IntDecimal
    case LongType => LongDecimal
    case FloatType => FloatDecimal
    case DoubleType => DoubleDecimal
  }

  def fromBigDecimal(d: BigDecimal): DecimalType = {
    DecimalType(Math.max(d.precision, d.scale), d.scale)
  }

  def bounded(precision: Int, scale: Int): DecimalType = {
    DecimalType(min(precision, MAX_PRECISION), min(scale, MAX_SCALE))
  }

  /**
   * Scale adjustment implementation is based on Hive's one, which is itself inspired to
   * SQLServer's one. In particular, when a result precision is greater than
   * {@link #MAX_PRECISION}, the corresponding scale is reduced to prevent the integral part of a
   * result from being truncated.
   *
   * This method is used only when `spark.sql.decimalOperations.allowPrecisionLoss` is set to true.
   */
  def adjustPrecisionScale(precision: Int, scale: Int): DecimalType = {
    // Assumption:
    assert(precision >= scale)

    if (precision <= MAX_PRECISION) {
      // Adjustment only needed when we exceed max precision
      DecimalType(precision, scale)
    } else if (scale < 0) {
      // Decimal can have negative scale (SPARK-24468). In this case, we cannot allow a precision
      // loss since we would cause a loss of digits in the integer part.
      // In this case, we are likely to meet an overflow.
      DecimalType(MAX_PRECISION, scale)
    } else {
      // Precision/scale exceed maximum precision. Result must be adjusted to MAX_PRECISION.
      val intDigits = precision - scale
      // If original scale is less than MINIMUM_ADJUSTED_SCALE, use original scale value; otherwise
      // preserve at least MINIMUM_ADJUSTED_SCALE fractional digits
      val minScaleValue = Math.min(scale, MINIMUM_ADJUSTED_SCALE)
      // The resulting scale is the maximum between what is available without causing a loss of
      // digits for the integer part of the decimal and the minimum guaranteed scale, which is
      // computed above
      val adjustedScale = Math.max(MAX_PRECISION - intDigits, minScaleValue)

      DecimalType(MAX_PRECISION, adjustedScale)
    }
  }

  override def defaultConcreteType: DataType = SYSTEM_DEFAULT

  override def acceptsType(other: DataType): Boolean = {
    other.isInstanceOf[DecimalType]
  }

  override def simpleString: String = "decimal"

  /**
   * Returns if dt is a DecimalType that fits inside an int
   */
  def is32BitDecimalType(dt: DataType): Boolean = {
    dt match {
      case t: DecimalType =>
        t.precision <= Decimal.MAX_INT_DIGITS
      case _ => false
    }
  }

  /**
   * Returns if dt is a DecimalType that fits inside a long
   */
  def is64BitDecimalType(dt: DataType): Boolean = {
    dt match {
      case t: DecimalType =>
        t.precision <= Decimal.MAX_LONG_DIGITS
      case _ => false
    }
  }

  /**
   * Returns if dt is a DecimalType that doesn't fit inside a long
   */
  def isByteArrayDecimalType(dt: DataType): Boolean = {
    dt match {
      case t: DecimalType =>
        t.precision > Decimal.MAX_LONG_DIGITS
      case _ => false
    }
  }

  def unapply(t: DataType): Boolean = t.isInstanceOf[DecimalType]

  def unapply(e: Expression): Boolean = e.dataType.isInstanceOf[DecimalType]

  object Fixed {
    def unapply(t: DecimalType): Option[(Int, Int)] = Some((t.precision, t.scale))
  }

  object Expression {
    def unapply(e: Expression): Option[(Int, Int)] = e.dataType match {
      case t: DecimalType => Some((t.precision, t.scale))
      case _ => None
    }
  }

}
