package com.ebay.tdq.expressions

import java.text.DateFormat
import java.util.TimeZone

import com.ebay.tdq.types._

import scala.util.control.NonFatal

/**
 * @author juntzhang
 */
/**
 * Common base class for time zone aware expressions.
 */
trait TimeZoneAwareExpression extends Expression {
  val EBAY_TIMEZONE = "GMT-7"
  @transient lazy val timeZone: TimeZone = {
    if (timeZoneId.isEmpty) {
      DateTimeUtils.getTimeZone(EBAY_TIMEZONE)
    } else {
      DateTimeUtils.getTimeZone(timeZoneId.get)
    }
  }

  /** the timezone ID to be used to evaluate value. */
  def timeZoneId: Option[String]

  /** Returns a copy of this expression with the specified timeZoneId. */
  def withTimeZone(timeZoneId: String): TimeZoneAwareExpression
}

abstract class UnixTime
  extends BinaryExpression with TimeZoneAwareExpression with ExpectsInputTypes {

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(StringType, DateType, TimestampType), StringType)

  override def dataType: DataType = LongType

  override def nullable: Boolean = true

  private lazy val constFormat: String = right.call().asInstanceOf[String]
  private lazy val formatter: DateFormat =
    try {
      DateTimeUtils.newDateFormat(constFormat, timeZone)
    } catch {
      case NonFatal(_) => null
    }

  override def eval(input: InternalRow): Any = {
    val t = left.call(input)
    if (t == null) {
      null
    } else {
      left.dataType match {
        case DateType =>
          DateTimeUtils.daysToMillis(t.asInstanceOf[Int], timeZone) / 1000L
        case LongType =>
          t
        case TimestampType =>
          t.asInstanceOf[Long] / 1000000L
        case StringType if right.foldable =>
          if (constFormat == null || formatter == null) {
            null
          } else {
            try {
              formatter.parse(
                t.asInstanceOf[String]).getTime / 1000L
            } catch {
              case NonFatal(_) => null
            }
          }
        case StringType =>
          val f = right.call(input)
          if (f == null) {
            null
          } else {
            val formatString = f.asInstanceOf[String]
            try {
              DateTimeUtils.newDateFormat(formatString, timeZone).parse(
                t.asInstanceOf[String]).getTime / 1000L
            } catch {
              case NonFatal(_) => null
            }
          }
      }
    }
  }
}


case class UnixTimestamp(timeExp: Expression, format: Expression, timeZoneId: Option[String], cacheKey: Option[String])
  extends UnixTime {

  def this(timeExp: Expression, format: Expression, cacheKey: Option[String]) = this(timeExp, format, None, cacheKey)

  override def left: Expression = timeExp

  override def right: Expression = format

  override def withTimeZone(timeZoneId: String): TimeZoneAwareExpression =
    copy(timeZoneId = Option(timeZoneId))

  def this(time: Expression, cacheKey: Option[String]) = {
    this(time, Literal("yyyy-MM-dd HH:mm:ss"), cacheKey)
  }

  def this(cacheKey: Option[String]) = {
    this(CurrentTimestamp(), cacheKey)
  }

  override def prettyName: String = "unix_timestamp"

}

case class CurrentTimestamp() extends LeafExpression {
  override def nullable: Boolean = false

  override def dataType: DataType = TimestampType

  override def eval(input: InternalRow): Any = {
    System.currentTimeMillis() * 1000L
  }

  override def prettyName: String = "current_timestamp"

  override def cacheKey: Option[String] = None
}

case class ParseToTimestamp(left: Expression, format: Option[Expression], child: Expression, cacheKey: Option[String])
  extends UnaryExpression {

  def this(left: Expression, format: Expression, cacheKey: Option[String]) = {
    this(left, Option(format), Cast(UnixTimestamp(left, format, None, None), TimestampType), cacheKey)
  }

  def this(left: Expression, cacheKey: Option[String]) = this(left, None, Cast(left, TimestampType), cacheKey)

  def flatArguments: Iterator[Any] = Iterator(left, format)

  override def sql: String = {
    if (format.isDefined) {
      s"$prettyName(${left.sql}, ${format.get.sql})"
    } else {
      s"$prettyName(${left.sql})"
    }
  }

  override def nullSafeEval(input: Any): Any = input

  override def prettyName: String = "to_timestamp"

  override def dataType: DataType = TimestampType

}

