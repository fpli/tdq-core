package com.ebay.tdq.rules

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.rules.physical.TdqUDFs
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

/**
 * @author juntzhang
 */
object ExprFunctions {
  def opt(operatorName: String, operands: Array[AnyRef]): Any = {
    def formatNumber(ans: Double): Any = {
      operands(0) match {
        case Int =>
          ans.toInt
        case Long =>
          ans.toLong
        case Float =>
          ans.toFloat
        case Short =>
          ans.toShort
        case Byte =>
          ans.toByte
        case _ =>
          ans
      }
    }

    // todo deal with not match type convert
    def compareTo(): Double = {
      operands(0) match {
        case str: String =>
          str.compareTo(operands(1).asInstanceOf[String]).toDouble
        case num: Number =>
          num.doubleValue() - operands(1).asInstanceOf[Number].doubleValue()
        case _ =>
          throw new IllegalStateException("< Unexpected type: " + operands(0).getClass)
      }
    }

    operatorName.toUpperCase() match {
      case "REGEXP_EXTRACT" =>
        assert(operands.length == 3)
        TdqUDFs.regexpExtract(
          operands(0).asInstanceOf[String],
          operands(1).asInstanceOf[String],
          operands(2).asInstanceOf[Number].intValue()
        );
      case "TAG_EXTRACT" =>
        assert(operands.length == 2)
        TdqUDFs.extractTag(
          operands(0).asInstanceOf[RawEvent],
          operands(1).asInstanceOf[String]
        );
      case "SITE_ID" =>
        assert(operands.length == 1)
        TdqUDFs.siteId(
          operands(0).asInstanceOf[RawEvent]
        );
      case "PAGE_FAMILY" =>
        assert(operands.length == 1)
        TdqUDFs.pageFamily(
          operands(0).asInstanceOf[RawEvent]
        );
      // todo transfer all number to Decimal
      case "-" =>
        formatNumber(operands(0).asInstanceOf[Number].doubleValue() - operands(1).asInstanceOf[Number].doubleValue())
      case "+" =>
        formatNumber(operands(0).asInstanceOf[Number].doubleValue() + operands(1).asInstanceOf[Number].doubleValue())
      case "*" =>
        formatNumber(operands(0).asInstanceOf[Number].doubleValue() * operands(1).asInstanceOf[Number].doubleValue())
      case "/" | "/INT" =>
        if (operands(0) == null || operands(1) == null || operands(1) == 0) {
          return formatNumber(0d)
        }
        formatNumber(operands(0).asInstanceOf[Number].doubleValue() / operands(1).asInstanceOf[Number].doubleValue())
      case "CAST" =>
        // todo need support more type
        cast_string2Number(operands(0).asInstanceOf[String], operands(1).asInstanceOf[String]);
      case "NOT" =>
        !operands(0).asInstanceOf[Boolean]
      case "OR" =>
        operands(0).asInstanceOf[Boolean] || operands(1).asInstanceOf[Boolean]
      case "AND" =>
        operands(0).asInstanceOf[Boolean] && operands(1).asInstanceOf[Boolean]
      case "IS NULL" =>
        operands(0) == null
      case "IS NOT NULL" =>
        operands(0) != null
      case "IN" =>
        operands(1).asInstanceOf[java.util.Set[_]].contains(operands(0))
      case "NOT IN" =>
        !operands(1).asInstanceOf[java.util.Set[_]].contains(operands(0))
      case "=" =>
        operands(0).equals(operands(1))
      case "<" =>
        compareTo() < 0d
      case "<=" =>
        compareTo() <= 0d
      case ">" =>
        compareTo() > 0d
      case ">=" =>
        compareTo() >= 0d
      case "CHAR_LENGTH" | "CHARACTER_LENGTH" | "LENGTH" =>
        StringUtils.length(operands(0).asInstanceOf[String])
      case _ =>
        throw new IllegalStateException("Unexpected operator: " + operatorName)
    }
  }

  def cast_string2Number(value: String, `type`: String): Number = `type` match {
    case "TINYINT" =>
      NumberUtils.toByte(value)
    case "SMALLINT" =>
      NumberUtils.toShort(value)
    case "INTEGER" =>
      NumberUtils.toInt(value)
    case "BIGINT" =>
      NumberUtils.toLong(value)
    case "DOUBLE" =>
      NumberUtils.toDouble(value)
    case "FLOAT" =>
      NumberUtils.toFloat(value)
    case _ =>
      null
  }

  def main(args: Array[String]): Unit = {
    //    println(opt("112", "1"))
  }
}
