package com.ebay.tdq.rules

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.analysis.TypeCoercionRule
import com.ebay.tdq.types._
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * @author juntzhang
 */
case class ExpressionRegistry(tdqEnv: TdqEnv, getDataType: Array[String] => DataType) {
  val LOG: Logger = Logger.getLogger("ExpressionRegistry")

  lazy val registries = Seq(
    CalciteGrammarRegistry,
    TdqUdfRegistry,
    SojUdfRegistry
  )

  def parse(operatorName: String, operands: Array[Any], alias: String): Expression = {
    val cacheKey = Some(alias).filter(StringUtils.isNotBlank)
    LOG.debug(s"operatorName=[$operatorName], operands=[${operands.mkString(",")}], alias=[$alias]")
    val cxt = RegistryContext(tdqEnv, operatorName.toUpperCase(), operands, cacheKey, getDataType)
    val expr = registries.find(_.isDefinedAt(cxt)) match {
      case Some(registry) => registry.apply(cxt)
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
