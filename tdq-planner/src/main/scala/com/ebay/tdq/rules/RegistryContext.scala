package com.ebay.tdq.rules

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.types.DataType

/**
 * @author juntzhang
 */
case class RegistryContext(
  tdqEnv: TdqEnv,
  operatorName: String,
  operands: Array[Any],
  cacheKey: Option[String] = None,
  getDataType: Array[String] => DataType
)

object RegistryContext {
  def unapply(cxt: RegistryContext): Some[(String, Array[Any], Option[String])] = {
    Some(cxt.operatorName, cxt.operands, cxt.cacheKey)
  }

  def unapply2(cxt: RegistryContext): Some[(String, Array[Any])] = {
    Some(cxt.operatorName, cxt.operands)
  }

  def unapply(operatorName: String): RegistryContext = {
    RegistryContext(null, operatorName = operatorName, operands = null, getDataType = null)
  }
}
