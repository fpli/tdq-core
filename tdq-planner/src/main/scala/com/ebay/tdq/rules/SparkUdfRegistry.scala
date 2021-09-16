package com.ebay.tdq.rules

import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.{Count, Max, Min, Sum}
import com.ebay.tdq.types.{DataType, IntegerType}
import com.google.common.base.Preconditions

/**
 * @author juntzhang
 */
object SparkUdfRegistry extends DelegatingRegistry({
  case RegistryContext("-", operands, cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    Subtract(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("+", operands, cacheKey) =>
    Add(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("*", operands, cacheKey) =>
    Multiply(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("/" | "/INT", operands, cacheKey) =>
    Division.coerceTypes(Divide(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey))
  case RegistryContext("CAST", operands, cacheKey) =>
    Cast(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[DataType],
      cacheKey = cacheKey
    )

  case cxt@RegistryContext("MOD", _, _) => cxt.expression[Remainder]
  case cxt@RegistryContext("ABS", _, _) => cxt.expression[Abs]
  case RegistryContext("COALESCE", operands: Array[Any], cacheKey) =>
    Coalesce(operands.map(_.asInstanceOf[Expression]), cacheKey)
  case RegistryContext("REGEXP_EXTRACT", operands: Array[Any], cacheKey) =>
    if (operands.length > 2) {
      RegExpExtract(
        subject = operands.head.asInstanceOf[Expression],
        regexp = operands(1).asInstanceOf[Expression],
        idx = Cast(operands(2).asInstanceOf[Expression], IntegerType),
        cacheKey = cacheKey
      )
    } else if (operands.length == 2) {
      RegExpExtract(
        subject = operands.head.asInstanceOf[Expression],
        regexp = operands(1).asInstanceOf[Expression],
        cacheKey = cacheKey
      )
    } else {
      throw new IllegalStateException("Unexpected operator[REGEXP_EXTRACT] args")
    }
  case RegistryContext("CHAR_LENGTH" | "CHARACTER_LENGTH" | "LENGTH", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Length(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("UPPER", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Upper(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("LOWER", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Lower(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
  case RegistryContext("SUBSTRING" | "SUBSTR", operands: Array[Any], cacheKey) =>
    if (operands.length == 2) {
      new Substring(
        operands.head.asInstanceOf[Expression],
        operands(1).asInstanceOf[Expression],
        cacheKey = cacheKey
      )
    } else {
      Preconditions.checkArgument(operands.length == 3)
      Substring(
        operands.head.asInstanceOf[Expression],
        operands(1).asInstanceOf[Expression],
        operands(2).asInstanceOf[Expression],
        cacheKey = cacheKey
      )
    }

  case RegistryContext("CONCAT", operands: Array[Any], cacheKey) =>
    Concat(
      operands.map(_.asInstanceOf[Expression]),
      cacheKey = cacheKey
    )
  case RegistryContext("ELEMENT_AT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    ElementAt(
      operands(0).asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("SPLIT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    StringSplit(
      operands(0).asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("TRIM", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    // todo currently only support both
    StringTrim(operands(2).asInstanceOf[Expression], None, cacheKey = cacheKey)
  case RegistryContext("SUM", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Sum(operands.head.asInstanceOf[Expression], cacheKey)
  case RegistryContext("MAX", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Max(operands.head.asInstanceOf[Expression], cacheKey)
  case RegistryContext("MIN", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Min(operands.head.asInstanceOf[Expression], cacheKey)
  case RegistryContext("COUNT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Count(operands.head.asInstanceOf[Expression], cacheKey)

  // math functions
  case cxt@RegistryContext("ACOS", _, _) => cxt.expression[Acos]
  case cxt@RegistryContext("ASIN", _, _) => cxt.expression[Asin]
  case cxt@RegistryContext("ATAN", _, _) => cxt.expression[Atan]
  case cxt@RegistryContext("ATAN2", _, _) => cxt.expression[Atan2]
  case cxt@RegistryContext("BIN", _, _) => cxt.expression[Bin]
  case cxt@RegistryContext("BROUND", _, _) => cxt.expression[BRound]
  case cxt@RegistryContext("CBRT", _, _) => cxt.expression[Cbrt]
  case cxt@RegistryContext("CEIL" | "CEILING", _, _) => cxt.expression[Ceil]
  case cxt@RegistryContext("COS", _, _) => cxt.expression[Cos]
  case cxt@RegistryContext("COSH", _, _) => cxt.expression[Cosh]
  case cxt@RegistryContext("CONV", _, _) => cxt.expression[Conv]
  case cxt@RegistryContext("DEGREES", _, _) => cxt.expression[ToDegrees]
  case cxt@RegistryContext("E", _, _) => cxt.expression[EulerNumber]
  case cxt@RegistryContext("EXP", _, _) => cxt.expression[Exp]
  case cxt@RegistryContext("EXPM1", _, _) => cxt.expression[Expm1]
  case cxt@RegistryContext("FLOOR", _, _) => cxt.expression[Floor]
  case cxt@RegistryContext("FACTORIAL", _, _) => cxt.expression[Factorial]
  case cxt@RegistryContext("HEX", _, _) => cxt.expression[Hex]
  case cxt@RegistryContext("HYPOT", _, _) => cxt.expression[Hypot]
  case cxt@RegistryContext("LOG", _, _) => cxt.expression[Logarithm]
  case cxt@RegistryContext("LOG10", _, _) => cxt.expression[Log10]
  case cxt@RegistryContext("LOG1P", _, _) => cxt.expression[Log1p]
  case cxt@RegistryContext("LOG2", _, _) => cxt.expression[Log2]
  case cxt@RegistryContext("LN", _, _) => cxt.expression[Log]
  case cxt@RegistryContext("MOD", _, _) => cxt.expression[Remainder]
  case cxt@RegistryContext("NEGATIVE", _, _) => cxt.expression[UnaryMinus]
  case cxt@RegistryContext("PI", _, _) => cxt.expression[Pi]
  case cxt@RegistryContext("PMOD", _, _) => cxt.expression[Pmod]
  case cxt@RegistryContext("POSITIVE", _, _) => cxt.expression[UnaryPositive]
  case cxt@RegistryContext("POW" | "POWER", _, _) => cxt.expression[Pow]
  case cxt@RegistryContext("RADIANS", _, _) => cxt.expression[ToRadians]
  case cxt@RegistryContext("RINT", _, _) => cxt.expression[Rint]
  //  case cxt@RegistryContext("ROUND", _, _) => cxt.expression[Round]
  case RegistryContext("ROUND", operands: Array[Any], cacheKey) =>
    if (operands.length == 1) {
      new Round(operands.head.asInstanceOf[Expression], cacheKey)
    } else if (operands.length == 2) {
      Round(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey)
    } else {
      throw new IllegalArgumentException()
    }
  case cxt@RegistryContext("SHIFTLEFT", _, _) => cxt.expression[ShiftLeft]
  case cxt@RegistryContext("SHIFTRIGHT", _, _) => cxt.expression[ShiftRight]
  case cxt@RegistryContext("SHIFTRIGHTUNSIGNED", _, _) => cxt.expression[ShiftRightUnsigned]
  case cxt@RegistryContext("SIGN" | "SIGNUM", _, _) => cxt.expression[Signum]
  case cxt@RegistryContext("SIN", _, _) => cxt.expression[Sin]
  case cxt@RegistryContext("SINH", _, _) => cxt.expression[Sinh]
  //  case cxt@RegistryConteXT("STR_TO_MAP",_,_) => cxt.expression[StringToMap] todo str_to_map
  case cxt@RegistryContext("SQRT", _, _) => cxt.expression[Sqrt]
  case cxt@RegistryContext("TAN", _, _) => cxt.expression[Tan]
  case cxt@RegistryContext("COT", _, _) => cxt.expression[Cot]
  case cxt@RegistryContext("TANH", _, _) => cxt.expression[Tanh]
})
