package com.ebay.tdq.rules

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate._
import com.ebay.tdq.expressions.analysis.TypeCoercionRule
import com.ebay.tdq.types._
import com.google.common.base.Preconditions
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * @author juntzhang
 */
case class ExpressionRegistry(tdqEnv: TdqEnv, getDataType: Array[String] => DataType) {
  val LOG: Logger = Logger.getLogger("ExpressionRegistry")

  def parse(operatorName: String, operands: Array[Any], alias: String): Expression = {
    val cacheKey = Some(alias).filter(StringUtils.isNotBlank)
    LOG.debug(s"operatorName=[$operatorName], operands=[${operands.mkString(",")}], alias=[$alias]")
    val expr = operatorName.toUpperCase() match {

      case "SOJ_PARSE_RLOGID" =>
        Preconditions.checkArgument(operands.length == 2)
        SojParseRlogid(
          operands.head.asInstanceOf[Expression],
          operands(1).asInstanceOf[Expression],
          cacheKey
        )
      case "SOJ_TAG" =>
        Preconditions.checkArgument(operands.length == 1)
        SojTag(
          subject = GetTdqEvent(Some("__TDQ_EVENT")),
          tag = operands.head.asInstanceOf[Literal].value.asInstanceOf[String],
          dataType = StringType,
          cacheKey = cacheKey
        )
      case "SOJ_NVL" =>
        Preconditions.checkArgument(operands.length == 1)
        SojNvl(
          subject = GetTdqEvent(Some("__TDQ_EVENT")),
          tag = operands.head.asInstanceOf[Literal].value.asInstanceOf[String],
          dataType = StringType,
          cacheKey = cacheKey
        )
      case "SOJ_PAGE_FAMILY" =>
        Preconditions.checkArgument(operands.length == 1)
        SojPageFamily(
          subject = operands.head.asInstanceOf[Expression],
          cacheKey = cacheKey,
          tdqEnv
        )
      case "SOJ_TIMESTAMP" =>
        GetTdqField("soj_timestamp", LongType)

      case "IS_BBWOA_PAGE_WITH_ITM" =>
        Preconditions.checkArgument(operands.length == 1)
        IsBBWOAPageWithItm(
          subject = operands.head.asInstanceOf[Expression],
          cacheKey = cacheKey,
          tdqEnv
        )
      case "EVENT_TIMESTAMP" =>
        GetTdqField("event_timestamp", TimestampType)
      case "EVENT_TIME_MILLIS" =>
        GetTdqField("event_time_millis", LongType)

      case "CURRENT_TIMESTAMP" =>
        CurrentTimestamp()
      case "UNIX_TIMESTAMP" =>
        if (operands.length == 0) {
          new UnixTimestamp(cacheKey)
        } else if (operands.length == 1) {
          new UnixTimestamp(operands.head.asInstanceOf[Expression], cacheKey)
        } else if (operands.length == 2) {
          new UnixTimestamp(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey)
        } else {
          UnixTimestamp(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], Some(operands(2).asInstanceOf[String]), cacheKey)
        }
      case "TO_TIMESTAMP" =>
        val left = operands.head.asInstanceOf[Expression]
        if (operands.length == 1) {
          new ParseToTimestamp(left, cacheKey)
        } else if (operands.length == 2) {
          new ParseToTimestamp(left, operands(1).asInstanceOf[Expression], cacheKey)
        } else {
          ParseToTimestamp(left, Option(operands(1).asInstanceOf[Expression]), operands(2).asInstanceOf[Expression], cacheKey)
        }
      case "ITEM" =>
        val names = operands.map {
          case field: GetTdqField =>
            field.name
          case literal: Literal =>
            literal.value.toString
          case t =>
            throw new IllegalStateException("Unexpected ITEM: " + t)
        }
        GetTdqField0(names, getDataType(names))

      // https://stackoverflow.com/questions/51860219/how-to-use-apache-calcite-like-regex
      // https://calcite.apache.org/docs/reference.html#keywords
      case "NOT RLIKE" | "NOT SIMILAR TO" =>
        Not(
          RLike(
            left = operands.head.asInstanceOf[Expression],
            right = operands(1).asInstanceOf[Expression],
            cacheKey = cacheKey
          )
        )
      case "RLIKE" | "SIMILAR TO" =>
        Preconditions.checkArgument(operands.length == 2)
        RLike(
          left = operands.head.asInstanceOf[Expression],
          right = operands(1).asInstanceOf[Expression],
          cacheKey = cacheKey
        )
      case "LIKE" =>
        Preconditions.checkArgument(operands.length == 2)
        Like(
          left = operands.head.asInstanceOf[Expression],
          right = operands(1).asInstanceOf[Expression],
          cacheKey = cacheKey
        )
      case "NOT LIKE" =>
        Preconditions.checkArgument(operands.length == 2)
        Not(
          Like(
            left = operands.head.asInstanceOf[Expression],
            right = operands(1).asInstanceOf[Expression],
            cacheKey = cacheKey
          )
        )
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
      case "-" =>
        Preconditions.checkArgument(operands.length == 2)
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
        Preconditions.checkArgument(operands.length == 2)
        Or(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "AND" =>
        And(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "COALESCE" =>
        Coalesce(operands.map(_.asInstanceOf[Expression]), cacheKey)
      case "IS NULL" =>
        Preconditions.checkArgument(operands.length == 1)
        IsNull(operands.head.asInstanceOf[Expression])
      case "IS NOT NULL" =>
        Preconditions.checkArgument(operands.length == 1)
        IsNotNull(operands.head.asInstanceOf[Expression])
      case "IN" =>
        Preconditions.checkArgument(operands.length == 2)
        // type match check
        In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]])
      case "NOT IN" =>
        Not(In(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Seq[Expression]]))
      case "=" =>
        Preconditions.checkArgument(operands.length == 2)
        EqualTo(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case ">" =>
        Preconditions.checkArgument(operands.length == 2)
        GreaterThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case ">=" =>
        Preconditions.checkArgument(operands.length == 2)
        GreaterThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "<" =>
        Preconditions.checkArgument(operands.length == 2)
        LessThan(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "<=" =>
        Preconditions.checkArgument(operands.length == 2)
        LessThanOrEqual(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression])
      case "CHAR_LENGTH" | "CHARACTER_LENGTH" | "LENGTH" =>
        Preconditions.checkArgument(operands.length == 1)
        Length(child = operands.head.asInstanceOf[Expression], cacheKey = cacheKey)
      //      case "TRIM" =>
      //        Preconditions.checkArgument(operands.length == 1)
      //        StringTrim(operands.head.asInstanceOf[Expression], None, cacheKey = cacheKey)
      case "SUM" =>
        Preconditions.checkArgument(operands.length == 1)
        Sum(operands.head.asInstanceOf[Expression], cacheKey)
      case "MAX" =>
        Preconditions.checkArgument(operands.length == 1)
        Max(operands.head.asInstanceOf[Expression], cacheKey)
      case "MIN" =>
        Preconditions.checkArgument(operands.length == 1)
        Min(operands.head.asInstanceOf[Expression], cacheKey)
      case "COUNT" =>
        Preconditions.checkArgument(operands.length == 1)
        Count(operands.head.asInstanceOf[Expression], cacheKey)
      case _ =>
        throw new IllegalStateException("Unexpected operator: " + operatorName)
    }

    TypeCoercionRule.coerceTypes(expr)
  }

}

object ExpressionRegistry {
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
