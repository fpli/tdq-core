package com.ebay.tdq.expressions

import java.math.{BigDecimal => JavaBigDecimal}

import com.ebay.tdq.rules.CalendarInterval
import com.ebay.tdq.types._
import com.ebay.tdq.util.StringUtils
import org.apache.commons.lang.math.NumberUtils

/**
 * @author juntzhang
 */
object Cast {

  /**
   * Returns true iff we can cast `from` type to `to` type.
   */
  def canCast(from: DataType, to: DataType): Boolean = (from, to) match {
    case (fromType, toType) if fromType == toType => true

    case (NullType, _) => true

    case (_, StringType) => true

    case (StringType, BinaryType) => true

    case (StringType, BooleanType) => true
    case (DateType, BooleanType) => true
    case (TimestampType, BooleanType) => true
    case (_: NumericType, BooleanType) => true

    case (StringType, TimestampType) => true
    case (BooleanType, TimestampType) => true
    case (DateType, TimestampType) => true
    case (_: NumericType, TimestampType) => true

    case (StringType, DateType) => true
    case (TimestampType, DateType) => true

    case (StringType, CalendarIntervalType) => true

    case (StringType, _: NumericType) => true
    case (BooleanType, _: NumericType) => true
    case (DateType, _: NumericType) => true
    case (TimestampType, _: NumericType) => true
    case (_: NumericType, _: NumericType) => true
    case _ => false
  }

  /**
   * Return true if we need to use the `timeZone` information casting `from` type to `to` type.
   * The patterns matched reflect the current implementation in the Cast node.
   * c.f. usage of `timeZone` in:
   * * Cast.castToString
   * * Cast.castToDate
   * * Cast.castToTimestamp
   */
  def needsTimeZone(from: DataType, to: DataType): Boolean = (from, to) match {
    case (StringType, TimestampType) => true
    case (DateType, TimestampType) => true
    case (TimestampType, StringType) => true
    case (TimestampType, DateType) => true
    case _ => false
  }

  /**
   * Return true iff we may truncate during casting `from` type to `to` type. e.g. long -> int,
   * timestamp -> date.
   */
  def mayTruncate(from: DataType, to: DataType): Boolean = (from, to) match {
    case (from: NumericType, to: DecimalType) if !to.isWiderThan(from) => true
    case (from: DecimalType, to: NumericType) if !from.isTighterThan(to) => true
    case (from, to) if illegalNumericPrecedence(from, to) => true
    case (TimestampType, DateType) => true
    case (StringType, to: NumericType) => true
    case _ => false
  }

  private def illegalNumericPrecedence(from: DataType, to: DataType): Boolean = {
    val fromPrecedence = TypeCoercion.numericPrecedence.indexOf(from)
    val toPrecedence = TypeCoercion.numericPrecedence.indexOf(to)
    toPrecedence >= 0 && fromPrecedence > toPrecedence
  }

  def forceNullable(from: DataType, to: DataType): Boolean = (from, to) match {
    case (NullType, _) => true
    case (_, _) if from == to => false

    case (StringType, BinaryType) => false
    case (StringType, _) => true
    case (_, StringType) => false

    case (FloatType | DoubleType, TimestampType) => true
    case (TimestampType, DateType) => false
    case (_, DateType) => true
    case (DateType, TimestampType) => false
    case (DateType, _) => true
    case (_, CalendarIntervalType) => true

    case (_, _: DecimalType) => true // overflow
    case (_: FractionalType, _: IntegralType) => true // NaN, infinity
    case _ => false
  }

  private def resolvableNullability(from: Boolean, to: Boolean) = !from || to
}

case class Cast(child: Expression, dataType: DataType, cacheKey: Option[String] = None,
  timeZoneId: Option[String] = None, forPrecision: Boolean = false)
  extends UnaryExpression with TimeZoneAwareExpression with NullIntolerant {

  private[this] lazy val cast: Any => Any = cast(child.dataType, dataType)

  def this(child: Expression, dataType: DataType, cacheKey: Option[String], timeZoneId: Option[String]) =
    this(child, dataType, cacheKey, timeZoneId, false)

  def this(child: Expression, dataType: DataType) = this(child, dataType, None)

  override def toString: String = s"cast($child as ${dataType.simpleString})"

  override def checkInputDataTypes(): TypeCheckResult = {
    if (Cast.canCast(child.dataType, dataType)) {
      TypeCheckResult.TypeCheckSuccess
    } else {
      TypeCheckResult.TypeCheckFailure(
        s"cannot cast ${child.dataType.simpleString} to ${dataType.simpleString}")
    }
  }

  override def nullable: Boolean = Cast.forceNullable(child.dataType, dataType) || child.nullable

  override def withTimeZone(timeZoneId: String): TimeZoneAwareExpression =
    copy(timeZoneId = Option(timeZoneId))

  override def sql: String = dataType match {
    // HiveQL doesn't allow casting to complex types. For logical plans translated from HiveQL, this
    // type of casting can only be introduced by the analyzer, and can be omitted when converting
    // back to SQL query string.
    case _ => s"CAST(${child.sql} AS ${dataType.sql})"
  }

  override def nullSafeEval(input: Any): Any = cast(input)

  private[this] def needsTimeZone: Boolean = Cast.needsTimeZone(child.dataType, dataType)

  private[this] def cast(from: DataType, to: DataType): Any => Any = {
    // If the cast does not change the structure, then we don't really need to cast anything.
    // We can return what the children return. Same thing should happen in the codegen path.
    if (from == to) {
      identity
    } else {
      to match {
        case dt if dt == from => identity[Any]
        case StringType => castToString(from)
        case BinaryType => castToBinary(from)
        case DateType => castToDate(from)
        case decimal: DecimalType => castToDecimal(from, decimal)
        case TimestampType => castToTimestamp(from)
        case CalendarIntervalType => castToInterval(from)
        case BooleanType => castToBoolean(from)
        case ByteType => castToByte(from)
        case ShortType => castToShort(from)
        case IntegerType => castToInt(from)
        case FloatType => castToFloat(from)
        case LongType => castToLong(from)
        case DoubleType => castToDouble(from)
        case _ =>
          throw new Exception(s"Cannot cast $from to $to.")
      }
    }
  }

  // UDFToString
  private[this] def castToString(from: DataType): Any => Any = from match {
    case BinaryType => buildCast[Array[Byte]](_, bytes => new String(bytes))
    case DateType => buildCast[Int](_, d => (DateTimeUtils.dateToString(d)))
    case TimestampType => buildCast[Long](_,
      t => (DateTimeUtils.timestampToString(t, timeZone)))
    case _ => buildCast[Any](_, o => (o.toString))
  }

  // BinaryConverter
  private[this] def castToBinary(from: DataType): Any => Any = from match {
    case StringType => buildCast[String](_, _.getBytes)
  }

  // UDFToBoolean
  private[this] def castToBoolean(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => {
        if (StringUtils.isTrueString(s)) {
          true
        } else if (StringUtils.isFalseString(s)) {
          false
        } else {
          null
        }
      })
    case TimestampType =>
      buildCast[Long](_, t => t != 0)
    case DateType =>
      // Hive would return null when cast from date to boolean
      buildCast[Int](_, d => null)
    case LongType =>
      buildCast[Long](_, _ != 0)
    case IntegerType =>
      buildCast[Int](_, _ != 0)
    case ShortType =>
      buildCast[Short](_, _ != 0)
    case ByteType =>
      buildCast[Byte](_, _ != 0)
    case DecimalType() =>
      buildCast[Decimal](_, !_.isZero)
    case DoubleType =>
      buildCast[Double](_, _ != 0)
    case FloatType =>
      buildCast[Float](_, _ != 0)
  }

  // TimestampConverter
  private[this] def castToTimestamp(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, utfs => DateTimeUtils.stringToTimestamp(utfs, timeZone).orNull)
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1L else 0)
    case LongType =>
      buildCast[Long](_, l => longToTimestamp(l))
    case IntegerType =>
      buildCast[Int](_, i => longToTimestamp(i.toLong))
    case ShortType =>
      buildCast[Short](_, s => longToTimestamp(s.toLong))
    case ByteType =>
      buildCast[Byte](_, b => longToTimestamp(b.toLong))
    case DateType =>
      buildCast[Int](_, d => DateTimeUtils.daysToMillis(d, timeZone) * 1000)
    // TimestampWritable.decimalToTimestamp
    case DecimalType() =>
      buildCast[Decimal](_, d => decimalToTimestamp(d))
    // TimestampWritable.doubleToTimestamp
    case DoubleType =>
      buildCast[Double](_, d => doubleToTimestamp(d))
    // TimestampWritable.floatToTimestamp
    case FloatType =>
      buildCast[Float](_, f => doubleToTimestamp(f.toDouble))
  }

  private[this] def decimalToTimestamp(d: Decimal): Long = {
    (d.toBigDecimal * 1000000L).longValue()
  }

  private[this] def doubleToTimestamp(d: Double): Any = {
    if (d.isNaN || d.isInfinite) null else (d * 1000000L).toLong
  }

  // converting seconds to us
  private[this] def longToTimestamp(t: Long): Long = t * 1000000L

  // DateConverter
  private[this] def castToDate(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => DateTimeUtils.stringToDate(s).orNull)
    case TimestampType =>
      // throw valid precision more than seconds, according to Hive.
      // Timestamp.nanos is in 0 to 999,999,999, no more than a second.
      buildCast[Long](_, t => DateTimeUtils.millisToDays(t / 1000L, timeZone))
  }

  // IntervalConverter
  private[this] def castToInterval(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => CalendarInterval.fromString(s.toString))
  }

  // [[func]] assumes the input is no longer null because eval already does the null check.
  @inline private[this] def buildCast[T](a: Any, func: T => Any): Any = func(a.asInstanceOf[T])

  // LongConverter
  private[this] def castToLong(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => if (NumberUtils.isNumber(s)) s.toLong else null)
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1L else 0L)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToLong(t))
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toLong(b)
  }

  // IntConverter
  private[this] def castToInt(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => if (NumberUtils.isNumber(s)) s.toInt else null)
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1 else 0)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToLong(t).toInt)
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toInt(b)
  }

  // converting us to seconds
  private[this] def timestampToLong(ts: Long): Long = math.floor(ts.toDouble / 1000000L).toLong

  // ShortConverter
  private[this] def castToShort(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => if (NumberUtils.isNumber(s)) {
        s.toShort
      } else {
        null
      })
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1.toShort else 0.toShort)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToLong(t).toShort)
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toInt(b).toShort
  }

  // ByteConverter
  private[this] def castToByte(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => if (NumberUtils.isNumber(s)) {
        s.toByte
      } else {
        null
      })
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1.toByte else 0.toByte)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToLong(t).toByte)
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toInt(b).toByte
  }

  private[this] def castToDecimal(from: DataType, target: DecimalType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => try {
        changePrecision(Decimal(new JavaBigDecimal(s.toString)), target)
      } catch {
        case _: NumberFormatException => null
      })
    case BooleanType =>
      buildCast[Boolean](_, b => toPrecision(if (b) Decimal.ONE else Decimal.ZERO, target))
    case DateType =>
      buildCast[Int](_, d => null) // date can't cast to decimal in Hive
    case TimestampType =>
      // Note that we lose precision here.
      buildCast[Long](_, t => changePrecision(Decimal(timestampToDouble(t)), target))
    case dt: DecimalType =>
      b => toPrecision(b.asInstanceOf[Decimal], target)
    case t: IntegralType =>
      b => changePrecision(Decimal(t.integral.asInstanceOf[Integral[Any]].toLong(b)), target)
    case x: FractionalType =>
      b =>
        try {
          changePrecision(Decimal(x.fractional.asInstanceOf[Fractional[Any]].toDouble(b)), target)
        } catch {
          case _: NumberFormatException => null
        }
  }

  /**
   * Change the precision / scale in a given decimal to those set in `decimalType` (if any),
   * returning null if it overflows or modifying `value` in-place and returning it if successful.
   *
   * NOTE: this modifies `value` in-place, so don't call it on external data.
   */
  private[this] def changePrecision(value: Decimal, decimalType: DecimalType): Decimal = {
    if (value.changePrecision(decimalType.precision, decimalType.scale)) value else null
  }

  /**
   * Create new `Decimal` with precision and scale given in `decimalType` (if any),
   * returning null if it overflows or creating a new `value` and returning it if successful.
   */
  private[this] def toPrecision(value: Decimal, decimalType: DecimalType): Decimal =
    value.toPrecision(decimalType.precision, decimalType.scale)

  // DoubleConverter
  private[this] def castToDouble(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => try s.toString.toDouble catch {
        case _: NumberFormatException => null
      })
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1d else 0d)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToDouble(t))
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toDouble(b)
  }

  // converting us to seconds in double
  private[this] def timestampToDouble(ts: Long): Double = {
    ts / 1000000.0
  }

  // FloatConverter
  private[this] def castToFloat(from: DataType): Any => Any = from match {
    case StringType =>
      buildCast[String](_, s => try s.toString.toFloat catch {
        case _: NumberFormatException => null
      })
    case BooleanType =>
      buildCast[Boolean](_, b => if (b) 1f else 0f)
    case DateType =>
      buildCast[Int](_, d => null)
    case TimestampType =>
      buildCast[Long](_, t => timestampToDouble(t).toFloat)
    case x: NumericType =>
      b => x.numeric.asInstanceOf[Numeric[Any]].toFloat(b)
  }

  private[this] def changePrecision(d: String, decimalType: DecimalType,
    evPrim: String, evNull: String): String =
    s"""
      if ($d.changePrecision(${decimalType.precision}, ${decimalType.scale})) {
        $evPrim = $d;
      } else {
        $evNull = true;
      }
    """
}

