package com.ebay.tdq.rules

import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate._
import com.ebay.tdq.types._
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * @author juntzhang
 */
object ExpressionRegistry {
  val LOG: Logger = Logger.getLogger("ExpressionRegistry")

  def parse(operatorName: String, operands: Array[Any], alias: String): Expression = {
    val cacheKey = Some(alias).filter(StringUtils.isNotBlank)
    LOG.info(s"operatorName=[$operatorName], operands=[${operands.mkString(",")}], alias=[$alias]")
    operatorName.toUpperCase() match {
      case "REGEXP_EXTRACT" =>
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
      case "TAG_EXTRACT" =>
        assert(operands.length == 1)
        ExtractTag(
          subject = GetRawEvent(),
          tag = operands.head.asInstanceOf[Literal].value.asInstanceOf[String],
          dataType = StringType,
          cacheKey = cacheKey
        )
      case "SITE_ID" =>
        ExtractTag(
          subject = GetRawEvent(),
          tag = "t",
          dataType = StringType,
          cacheKey = cacheKey
        )
      case "PAGE_FAMILY" =>
        assert(operands.length == 1)
        PageFamily(
          subject = operands.head.asInstanceOf[Expression],
          cacheKey = cacheKey
        )
      case "-" =>
        assert(operands.length == 2)
        Subtract(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
      case "+" =>
        Add(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
      case "*" =>
        Multiply(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey)
      case "/" | "/INT" =>
        Division.coerceTypes(Divide(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey = cacheKey))
      case "CAST" =>
        Cast(
          operands.head.asInstanceOf[Expression],
          operands(1).asInstanceOf[DataType],
          cacheKey = cacheKey
        )
      case "NOT" =>
        Not(operands.head.asInstanceOf[Expression])
      case "OR" =>
        assert(operands.length == 2)
        Or(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "AND" =>
        And(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "IS NULL" =>
        assert(operands.length == 1)
        IsNull(operands.head.asInstanceOf[Expression])
      case "IS NOT NULL" =>
        assert(operands.length == 1)
        IsNotNull(operands.head.asInstanceOf[Expression])
      case "IN" =>
        assert(operands.length == 2)
        // type match check
        In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]])
      case "NOT IN" =>
        Not(In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]]))
      case "=" =>
        assert(operands.length == 2)
        EqualTo(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case ">" =>
        assert(operands.length == 2)
        GreaterThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case ">=" =>
        assert(operands.length == 2)
        GreaterThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "<" =>
        assert(operands.length == 2)
        LessThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "<=" =>
        assert(operands.length == 2)
        LessThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "CHAR_LENGTH" | "CHARACTER_LENGTH" | "LENGTH" =>
        assert(operands.length == 1)
        Length(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
      //      case "TRIM" =>
      //        assert(operands.length == 1)
      //        StringTrim(operands.head.asInstanceOf[Expression], None, cacheKey = cacheKey)
      case "SUM" =>
        assert(operands.length == 1)
        Sum(operands.head.asInstanceOf[Expression], cacheKey)
      case "MAX" =>
        assert(operands.length == 1)
        Max(operands.head.asInstanceOf[Expression], cacheKey)
      case "MIN" =>
        assert(operands.length == 1)
        Min(operands.head.asInstanceOf[Expression], cacheKey)
      case "COUNT" =>
        assert(operands.length == 1)
        Count(operands.head.asInstanceOf[Expression], cacheKey)
      case _ =>
        throw new IllegalStateException("Unexpected operator: " + operatorName)
    }
  }

  def aggregateOperator(operatorName: String, v1: Double, v2: Double): Double = {
    operatorName.toUpperCase() match {
      case "COUNT" =>
        return v1 + v2
      case "SUM" =>
        return v1 + v2
      case "MAX" =>
        return Math.max(v1, v2)
      case "MIN" =>
        return Math.min(v1, v2)
      case _ =>
        throw new IllegalStateException("Unexpected operator: " + operatorName)
    }
    v1 + v2
  }
}
