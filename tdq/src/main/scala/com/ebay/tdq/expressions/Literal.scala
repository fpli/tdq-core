package com.ebay.tdq.expressions

import java.math.{BigDecimal => JavaBigDecimal}
import java.sql.{Date, Timestamp}
import java.util.Objects

import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
/**
 * In order to do type checking, use Literal.create() instead of constructor
 */
object Literal {
  val TrueLiteral: Literal = Literal(true, BooleanType)

  val FalseLiteral: Literal = Literal(false, BooleanType)

  def apply(v: Any): Literal = v match {
    case i: Int => Literal(i, IntegerType)
    case l: Long => Literal(l, LongType)
    case d: Double => Literal(d, DoubleType)
    case f: Float => Literal(f, FloatType)
    case b: Byte => Literal(b, ByteType)
    case s: Short => Literal(s, ShortType)
    case s: String => Literal(s, StringType)
    case b: Boolean => Literal(b, BooleanType)
    case d: BigDecimal => Literal(Decimal(d), DecimalType.fromBigDecimal(d))
    case d: JavaBigDecimal =>
      Literal(Decimal(d), DecimalType(Math.max(d.precision, d.scale), d.scale()))
    case d: Decimal => Literal(d, DecimalType(Math.max(d.precision, d.scale), d.scale))
    case t: Timestamp => Literal(DateTimeUtils.fromJavaTimestamp(t), TimestampType)
    case d: Date => Literal(DateTimeUtils.fromJavaDate(d), DateType)
    case a: Array[Byte] => Literal(a, BinaryType)
    case null => Literal(null, NullType)
    case v: Literal => v
    case _ =>
      throw new RuntimeException("Unsupported literal type " + v.getClass + " " + v)
  }
}


case class Literal(value: Any, dataType: DataType) extends LeafExpression {
  override def nullable: Boolean = value == null

  override def toString: String = value match {
    case null => "null"
    case other => other.toString
  }

  override def hashCode(): Int = {
    val valueHashCode = value match {
      case null => 0
      case binary: Array[Byte] => java.util.Arrays.hashCode(binary)
      case other => other.hashCode()
    }
    31 * Objects.hashCode(dataType) + valueHashCode
  }

  override def equals(other: Any): Boolean = other match {
    case o: Literal if !dataType.equals(o.dataType) => false
    case o: Literal =>
      (value, o.value) match {
        case (null, null) => true
        case (a: Array[Byte], b: Array[Byte]) => java.util.Arrays.equals(a, b)
        case (a, b) => a != null && a.equals(b)
      }
    case _ => false
  }

  override def sql: String = value.toString

  override def cacheKey: Option[String] = None

  protected override def eval(input: InternalRow, fromCache: Boolean): Any = value
}

