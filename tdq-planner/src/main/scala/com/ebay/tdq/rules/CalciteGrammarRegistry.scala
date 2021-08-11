package com.ebay.tdq.rules

import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.{Count, Max, Min, Sum}
import com.ebay.tdq.types.{DataType, IntegerType}
import com.google.common.base.Preconditions

/**
 * https://stackoverflow.com/questions/51860219/how-to-use-apache-calcite-like-regex
 * https://calcite.apache.org/docs/reference.html#keywords
 *
 * @author juntzhang
 */
object CalciteGrammarRegistry extends DelegatingRegistry({
  case RegistryContext("NOT RLIKE" | "NOT SIMILAR TO", operands, cacheKey) =>
    Not(
      RLike(
        left = operands.head.asInstanceOf[Expression],
        right = operands(1).asInstanceOf[Expression],
        cacheKey = cacheKey
      )
    )
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
  case RegistryContext("RLIKE" | "SIMILAR TO", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    RLike(
      left = operands.head.asInstanceOf[Expression],
      right = operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("LIKE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    Like(
      left = operands.head.asInstanceOf[Expression],
      right = operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("NOT LIKE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    Not(
      Like(
        left = operands.head.asInstanceOf[Expression],
        right = operands(1).asInstanceOf[Expression],
        cacheKey = cacheKey
      )
    )
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
  case RegistryContext("NOT", operands: Array[Any], _) =>
    Not(operands.head.asInstanceOf[Expression])
  case RegistryContext("OR", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    Or(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext("AND", operands: Array[Any], _) =>
    And(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext("COALESCE", operands: Array[Any], cacheKey) =>
    Coalesce(operands.map(_.asInstanceOf[Expression]), cacheKey)
  case RegistryContext("IS NULL", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 1)
    IsNull(operands.head.asInstanceOf[Expression])
  case RegistryContext("IS NOT NULL", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 1)
    IsNotNull(operands.head.asInstanceOf[Expression])
  case RegistryContext("IN", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    // type match check
    In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]])
  case RegistryContext("NOT IN", operands: Array[Any], _) =>
    Not(In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]]))
  case RegistryContext("=", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    EqualTo(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext(">", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    GreaterThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext(">=", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    GreaterThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext("<", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    LessThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext("<=", operands: Array[Any], _) =>
    Preconditions.checkArgument(operands.length == 2)
    LessThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
  case RegistryContext("CHAR_LENGTH" | "CHARACTER_LENGTH" | "LENGTH", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Length(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
  //      case ("TRIM", operands: Array[Any], cacheKey) =>
  //        Preconditions.checkArgument(operands.length == 1)
  //        StringTrim(operands.head.asInstanceOf[Expression], None, cacheKey = cacheKey)
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
})
