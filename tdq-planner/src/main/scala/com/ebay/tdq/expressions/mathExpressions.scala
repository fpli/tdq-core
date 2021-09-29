package com.ebay.tdq.expressions

import java.util.Locale

import com.ebay.tdq.expressions.TypeCheckResult._
import com.ebay.tdq.planner.ExpressionDescription
import com.ebay.tdq.types._
import com.ebay.tdq.utils.CalendarInterval

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

abstract class UnaryMathExpression(val f: Double => Double, name: String)
  extends UnaryExpression with Serializable with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] = Seq(DoubleType)

  override def dataType: DataType = DoubleType

  override def nullable: Boolean = true

  override def toString: String = s"$name($child)"

  override def prettyName: String = name

  protected override def nullSafeEval(input: Any): Any = {
    f(input.asInstanceOf[Double])
  }

  // name of function in java.lang.Math
  def funcName: String = name.toLowerCase(Locale.ROOT)

}

abstract class BinaryMathExpression(f: (Double, Double) => Double, name: String)
  extends BinaryExpression with Serializable with ImplicitCastInputTypes {

  override def inputTypes: Seq[DataType] = Seq(DoubleType, DoubleType)

  override def toString: String = s"$name($left, $right)"

  override def prettyName: String = name

  override def dataType: DataType = DoubleType

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    f(input1.asInstanceOf[Double], input2.asInstanceOf[Double])
  }

}

abstract class UnaryLogExpression(f: Double => Double, name: String)
  extends UnaryMathExpression(f, name) {

  override def nullable: Boolean = true

  // values less than or equal to yAsymptote eval to null in Hive, instead of NaN or -Infinity
  protected val yAsymptote: Double = 0.0

  protected override def nullSafeEval(input: Any): Any = {
    val d = input.asInstanceOf[Double]
    if (d <= yAsymptote) null else f(d)
  }
}

abstract class LeafMathExpression(c: Double, name: String)
  extends LeafExpression with Serializable {

  override def dataType: DataType = DoubleType

  override def foldable: Boolean = true

  override def nullable: Boolean = false

  override def toString: String = s"$name()"

  override def prettyName: String = name

  override def eval(input: InternalRow): Any = c
}

case class Acos(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.acos, "ACOS")

case class Asin(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.asin, "ASIN")

case class Atan(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.atan, "ATAN")

case class Atan2(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryMathExpression(math.atan2, "ATAN2") {

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    // With codegen, the values returned by -0.0 and 0.0 are different. Handled with +0.0
    math.atan2(input1.asInstanceOf[Double] + 0.0, input2.asInstanceOf[Double] + 0.0)
  }
}

case class Bin(child: Expression, cacheKey: Option[String])
  extends UnaryExpression with Serializable with ImplicitCastInputTypes {

  override def inputTypes: Seq[DataType] = Seq(LongType)

  override def dataType: DataType = StringType

  protected override def nullSafeEval(input: Any): Any = java.lang.Long.toBinaryString(input.asInstanceOf[Long])

}

case class BRound(child: Expression, scale: Expression, cacheKey: Option[String])
  extends RoundBase(child, scale, BigDecimal.RoundingMode.HALF_EVEN, "ROUND_HALF_EVEN")
    with Serializable with ImplicitCastInputTypes {
  def this(child: Expression, cacheKey: Option[String]) = this(child, Literal(0), cacheKey)
}

case class Cbrt(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.cbrt, "CBRT")

// ceiling || ceil
case class Ceil(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.ceil, "CEIL") {
  override def dataType: DataType = child.dataType match {
    case dt@DecimalType.Fixed(_, 0) => dt
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision - scale + 1, 0)
    case _ => LongType
  }

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(DoubleType, DecimalType, LongType))

  protected override def nullSafeEval(input: Any): Any = child.dataType match {
    case LongType => input.asInstanceOf[Long]
    case DoubleType => f(input.asInstanceOf[Double]).toLong
    case DecimalType.Fixed(_, _) => input.asInstanceOf[Decimal].ceil
  }
}

case class Cos(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.cos, "COS")

case class Cosh(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.cosh, "COSH")

@ExpressionDescription(
  usage = "_FUNC_(num, from_base, to_base) - Convert `num` from `from_base` to `to_base`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_('100', 2, 10);
       4
      > SELECT _FUNC_(-10, 16, -10);
       -16
  """)
case class Conv(numExpr: Expression, fromBaseExpr: Expression, toBaseExpr: Expression, cacheKey: Option[String])
  extends TernaryExpression with ImplicitCastInputTypes {

  override def children: Seq[Expression] = Seq(numExpr, fromBaseExpr, toBaseExpr)

  override def inputTypes: Seq[AbstractDataType] = Seq(StringType, IntegerType, IntegerType)

  override def dataType: DataType = StringType

  override def nullable: Boolean = true

  override def nullSafeEval(num: Any, fromBase: Any, toBase: Any): Any = {
    NumberConverter.convert(
      num.asInstanceOf[String].getBytes,
      fromBase.asInstanceOf[Int],
      toBase.asInstanceOf[Int])
  }
}

case class ToDegrees(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.toDegrees, "DEGREES") {
  override def funcName: String = "toDegrees"
}

case class EulerNumber(cacheKey: Option[String]) extends LeafMathExpression(math.E, "E")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns e to the power of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       1.0
  """)
case class Exp(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.exp, "EXP")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns exp(`expr`) - 1.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Expm1(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.expm1, "EXPM1")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the largest integer not greater than `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(-0.1);
       -1
      > SELECT _FUNC_(5);
       5
  """)
case class Floor(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.floor, "FLOOR") {
  override def dataType: DataType = child.dataType match {
    case dt@DecimalType.Fixed(_, 0) => dt
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision - scale + 1, 0)
    case _ => LongType
  }

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(DoubleType, DecimalType, LongType))

  protected override def nullSafeEval(input: Any): Any = child.dataType match {
    case LongType => input.asInstanceOf[Long]
    case DoubleType => f(input.asInstanceOf[Double]).toLong
    case DecimalType.Fixed(_, _) => input.asInstanceOf[Decimal].floor
  }
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the factorial of `expr`. `expr` is [0..20]. Otherwise, null.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(5);
       120
  """)
case class Factorial(child: Expression, cacheKey: Option[String]) extends UnaryExpression with ImplicitCastInputTypes {

  override def inputTypes: Seq[DataType] = Seq(IntegerType)

  override def dataType: DataType = LongType

  // If the value not in the range of [0, 20], it still will be null, so set it to be true here.
  override def nullable: Boolean = true

  protected override def nullSafeEval(input: Any): Any = {
    val value = input.toString.toInt
    if (value > 20 || value < 0) {
      null
    } else {
      Factorial.factorial(value)
    }
  }
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Converts `expr` to hexadecimal.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(17);
       11
      > SELECT _FUNC_('Spark SQL');
       537061726B2053514C
  """)
case class Hex(child: Expression, cacheKey: Option[String]) extends UnaryExpression with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(LongType, BinaryType, StringType))

  override def dataType: DataType = StringType

  protected override def nullSafeEval(num: Any): Any = child.dataType match {
    case LongType => Hex.hex(num.asInstanceOf[Long])
    case BinaryType => Hex.hex(num.asInstanceOf[Array[Byte]])
    case StringType => Hex.hex(num.asInstanceOf[String].getBytes)
  }

}

@ExpressionDescription(
  usage = "_FUNC_(expr1, expr2) - Returns sqrt(`expr1`**2 + `expr2`**2).",
  examples =
    """
    Examples:
      > SELECT _FUNC_(3, 4);
       5.0
  """)
case class Hypot(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryMathExpression(math.hypot, "HYPOT")

/**
 * Computes the logarithm of a number.
 *
 * @param left  the logarithm base, default to e.
 * @param right the number to compute the logarithm of.
 */
@ExpressionDescription(
  usage = "_FUNC_(base, expr) - Returns the logarithm of `expr` with `base`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(10, 100);
       2.0
  """)
case class Logarithm(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryMathExpression((c1, c2) => math.log(c2) / math.log(c1), "LOG") {

  /**
   * Natural log, i.e. using e as the base.
   */
  def this(child: Expression, cacheKey: Option[String]) = {
    this(EulerNumber(cacheKey), child, cacheKey)
  }

  override def nullable: Boolean = true

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    val dLeft = input1.asInstanceOf[Double]
    val dRight = input2.asInstanceOf[Double]
    // Unlike Hive, we support Log base in (0.0, 1.0]
    if (dLeft <= 0.0 || dRight <= 0.0) null else math.log(dRight) / math.log(dLeft)
  }
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the logarithm of `expr` with base 10.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(10);
       1.0
  """)
case class Log10(child: Expression, cacheKey: Option[String]) extends UnaryLogExpression(math.log10, "LOG10")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns log(1 + `expr`).",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Log1p(child: Expression, cacheKey: Option[String]) extends UnaryLogExpression(math.log1p, "LOG1P") {
  protected override val yAsymptote: Double = -1.0
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the logarithm of `expr` with base 2.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(2);
       1.0
  """)
case class Log2(child: Expression, cacheKey: Option[String])
  extends UnaryLogExpression((x: Double) => math.log(x) / math.log(2), "LOG2") {
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the natural logarithm (base e) of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(1);
       0.0
  """)
case class Log(child: Expression, cacheKey: Option[String]) extends UnaryLogExpression(math.log, "LOG")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the negated value of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(1);
       -1
  """)
case class UnaryMinus(child: Expression, cacheKey: Option[String]) extends UnaryExpression
  with ExpectsInputTypes with NullIntolerant {

  override def inputTypes: Seq[AbstractDataType] = Seq(TypeCollection.NumericAndInterval)

  override def dataType: DataType = child.dataType

  override def toString: String = s"-$child"

  private lazy val numeric = TypeUtils.getNumeric(dataType)

  protected override def nullSafeEval(input: Any): Any = {
    if (dataType.isInstanceOf[CalendarIntervalType]) {
      input.asInstanceOf[CalendarInterval].negate()
    } else {
      numeric.negate(input)
    }
  }

  override def sql: String = s"(- ${child.sql})"
}

/**
 * Pi. Note that there is no code generation because this is only
 * evaluated by the optimizer during constant folding.
 */
@ExpressionDescription(
  usage = "_FUNC_() - Returns pi.",
  examples =
    """
    Examples:
      > SELECT _FUNC_();
       3.141592653589793
  """)
case class Pi(cacheKey: Option[String]) extends LeafMathExpression(math.Pi, "PI")

@ExpressionDescription(
  usage = "_FUNC_(expr1, expr2) - Returns the positive value of `expr1` mod `expr2`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(10, 3);
       1
      > SELECT _FUNC_(-10, 3);
       2
  """)
case class Pmod(left: Expression, right: Expression, cacheKey: Option[String]) extends BinaryArithmetic {

  override def toString: String = s"pmod($left, $right)"

  override def symbol: String = "pmod"

  override def inputType: AbstractDataType = NumericType

  override def nullable: Boolean = true

  override def eval(input: InternalRow): Any = {
    val input2 = right.call(input)
    if (input2 == null || input2 == 0) {
      null
    } else {
      val input1 = left.call(input)
      if (input1 == null) {
        null
      } else {
        input1 match {
          case i: Integer => pmod(i, input2.asInstanceOf[java.lang.Integer])
          case l: Long => pmod(l, input2.asInstanceOf[java.lang.Long])
          case s: Short => pmod(s, input2.asInstanceOf[java.lang.Short])
          case b: Byte => pmod(b, input2.asInstanceOf[java.lang.Byte])
          case f: Float => pmod(f, input2.asInstanceOf[java.lang.Float])
          case d: Double => pmod(d, input2.asInstanceOf[java.lang.Double])
          case d: Decimal => pmod(d, input2.asInstanceOf[Decimal])
        }
      }
    }
  }


  private def pmod(a: Int, n: Int): Int = {
    val r = a % n
    if (r < 0) {
      (r + n) % n
    } else r
  }

  private def pmod(a: Long, n: Long): Long = {
    val r = a % n
    if (r < 0) {
      (r + n) % n
    } else r
  }

  private def pmod(a: Byte, n: Byte): Byte = {
    val r = a % n
    if (r < 0) {
      ((r + n) % n).toByte
    } else r.toByte
  }

  private def pmod(a: Double, n: Double): Double = {
    val r = a % n
    if (r < 0) {
      (r + n) % n
    } else r
  }

  private def pmod(a: Short, n: Short): Short = {
    val r = a % n
    if (r < 0) {
      ((r + n) % n).toShort
    } else r.toShort
  }

  private def pmod(a: Float, n: Float): Float = {
    val r = a % n
    if (r < 0) {
      (r + n) % n
    } else r
  }

  private def pmod(a: Decimal, n: Decimal): Decimal = {
    val r = a % n
    if (r != null && r.compare(Decimal.ZERO) < 0) {
      (r + n) % n
    } else r
  }

  override def sql: String = s"$prettyName(${left.sql}, ${right.sql})"
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the value of `expr`.")
case class UnaryPositive(child: Expression, cacheKey: Option[String])
  extends UnaryExpression with ExpectsInputTypes with NullIntolerant {
  override def prettyName: String = "positive"

  override def inputTypes: Seq[AbstractDataType] = Seq(TypeCollection.NumericAndInterval)

  override def dataType: DataType = child.dataType

  protected override def nullSafeEval(input: Any): Any = input

  override def sql: String = s"(+ ${child.sql})"
}

@ExpressionDescription(
  usage = "_FUNC_(expr1, expr2) - Raises `expr1` to the power of `expr2`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(2, 3);
       8.0
  """)
case class Pow(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryMathExpression(math.pow, "POWER") {
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Converts degrees to radians.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(180);
       3.141592653589793
  """)
case class ToRadians(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.toRadians, "RADIANS") {
  override def funcName: String = "toRadians"
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the double value that is closest in value to the argument and is equal to a mathematical integer.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(12.3456);
       12.0
  """)
case class Rint(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.rint, "ROUND") {
  override def funcName: String = "rint"
}

@ExpressionDescription(
  usage = "_FUNC_(expr, d) - Returns `expr` rounded to `d` decimal places using HALF_UP rounding mode.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(2.5, 0);
       3.0
  """)
case class Round(child: Expression, scale: Expression, cacheKey: Option[String])
  extends RoundBase(child, scale, BigDecimal.RoundingMode.HALF_UP, "ROUND_HALF_UP")
    with Serializable with ImplicitCastInputTypes {
  def this(child: Expression, cacheKey: Option[String]) = this(child, Literal(0), cacheKey)
}

@ExpressionDescription(
  usage = "_FUNC_(base, expr) - Bitwise left shift.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(2, 1);
       4
  """)
case class ShiftLeft(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryExpression with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(IntegerType, LongType), IntegerType)

  override def dataType: DataType = left.dataType

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    input1 match {
      case l: java.lang.Long => l << input2.toString.toInt
      case i: java.lang.Integer => i << input2.toString.toInt
      case l2: Long => l2 << input2.toString.toInt
      case i2: Int => i2 << input2.toString.toInt
    }
  }
}

@ExpressionDescription(
  usage = "_FUNC_(base, expr) - Bitwise (signed) right shift.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(4, 1);
       2
  """)
case class ShiftRight(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryExpression with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(IntegerType, LongType), IntegerType)

  override def dataType: DataType = left.dataType

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    input1 match {
      case l: java.lang.Long => l >> input2.toString.toInt
      case i: java.lang.Integer => i >> input2.toString.toInt
      case l2: Long => l2 >> input2.toString.toInt
      case i2: Int => i2 >> input2.toString.toInt
    }
  }
}

@ExpressionDescription(
  usage = "_FUNC_(base, expr) - Bitwise unsigned right shift.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(4, 1);
       2
  """)
case class ShiftRightUnsigned(left: Expression, right: Expression, cacheKey: Option[String])
  extends BinaryExpression with ImplicitCastInputTypes {

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(IntegerType, LongType), IntegerType)

  override def dataType: DataType = left.dataType

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    input1 match {
      case l: java.lang.Long => l >>> input2.toString.toInt
      case i: java.lang.Integer => i >>> input2.toString.toInt
      case l2: Long => l2 >>> input2.toString.toInt
      case i2: Int => i2 >>> input2.toString.toInt
    }
  }
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns -1.0, 0.0 or 1.0 as `expr` is negative, 0 or positive.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(40);
       1.0
  """)
case class Signum(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.signum, "SIGNUM")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the sine of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Sin(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.sin, "SIN")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the hyperbolic sine of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Sinh(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.sinh, "SINH")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the square root of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(4);
       2.0
  """)
case class Sqrt(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.sqrt, "SQRT")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the tangent of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Tan(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.tan, "TAN")

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the cotangent of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(1);
       0.6420926159343306
  """)
case class Cot(child: Expression, cacheKey: Option[String])
  extends UnaryMathExpression((x: Double) => 1 / math.tan(x), "COT") {
}

@ExpressionDescription(
  usage = "_FUNC_(expr) - Returns the hyperbolic tangent of `expr`.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(0);
       0.0
  """)
case class Tanh(child: Expression, cacheKey: Option[String]) extends UnaryMathExpression(math.tanh, "TANH")


object Factorial {

  def factorial(n: Int): Long = {
    if (n < factorials.length) factorials(n) else Long.MaxValue
  }

  private val factorials: Array[Long] = Array[Long](
    1,
    1,
    2,
    6,
    24,
    120,
    720,
    5040,
    40320,
    362880,
    3628800,
    39916800,
    479001600,
    6227020800L,
    87178291200L,
    1307674368000L,
    20922789888000L,
    355687428096000L,
    6402373705728000L,
    121645100408832000L,
    2432902008176640000L
  )
}

object Hex {
  val hexDigits = Array[Char](
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  ).map(_.toByte)

  // lookup table to translate '0' -> 0 ... 'F'/'f' -> 15
  val unhexDigits = {
    val array = Array.fill[Byte](128)(-1)
    (0 to 9).foreach(i => array('0' + i) = i.toByte)
    (0 to 5).foreach(i => array('A' + i) = (i + 10).toByte)
    (0 to 5).foreach(i => array('a' + i) = (i + 10).toByte)
    array
  }

  def hex(bytes: Array[Byte]): String = {
    val length = bytes.length
    val value = new Array[Byte](length * 2)
    var i = 0
    while (i < length) {
      value(i * 2) = Hex.hexDigits((bytes(i) & 0xF0) >> 4)
      value(i * 2 + 1) = Hex.hexDigits(bytes(i) & 0x0F)
      i += 1
    }
    new String(value)
  }

  def hex(num: Long): String = {
    // Extract the hex digits of num into value[] from right to left
    val value = new Array[Byte](16)
    var numBuf = num
    var len = 0
    do {
      len += 1
      value(value.length - len) = Hex.hexDigits((numBuf & 0xF).toInt)
      numBuf >>>= 4
    } while (numBuf != 0)
    new String(java.util.Arrays.copyOfRange(value, value.length - len, value.length))
  }

  def unhex(bytes: Array[Byte]): Array[Byte] = {
    val out = new Array[Byte]((bytes.length + 1) >> 1)
    var i = 0
    if ((bytes.length & 0x01) != 0) {
      // padding with '0'
      if (bytes(0) < 0) {
        return null
      }
      val v = Hex.unhexDigits(bytes(0))
      if (v == -1) {
        return null
      }
      out(0) = v
      i += 1
    }
    // two characters form the hex value.
    while (i < bytes.length) {
      if (bytes(i) < 0 || bytes(i + 1) < 0) {
        return null
      }
      val first = Hex.unhexDigits(bytes(i))
      val second = Hex.unhexDigits(bytes(i + 1))
      if (first == -1 || second == -1) {
        return null
      }
      out(i / 2) = (((first << 4) | second) & 0xFF).toByte
      i += 2
    }
    out
  }
}