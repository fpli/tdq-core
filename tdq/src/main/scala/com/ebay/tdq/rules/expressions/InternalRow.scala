package com.ebay.tdq.rules.expressions

  import java.util.{HashMap => JHashMap}

/**
 * @author juntzhang
 */
case class InternalRow(operands: Array[Any], cachedData: JHashMap[String, Any]) {
  def cache(k: Option[String], v: Any): Unit = {
    if (k.isDefined && k.get.nonEmpty) {
      cachedData.put(k.get, v)
    }
  }
}
