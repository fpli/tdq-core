package com.ebay.tdq.expressions

import java.util.{HashMap => JHashMap}

/**
 * @author juntzhang
 */
case class InternalRow(operands: Array[Any], rowCache: JHashMap[String, Any]) {

  def containsKey(k: Option[String]): Boolean = {
    k.isDefined && k.get.nonEmpty && rowCache.containsKey(k)
  }

  def putCache(k: Option[String], v: Any): Unit = {
    if (k.isDefined && k.get.nonEmpty) {
      rowCache.put(k.get, v)
    }
  }

  def getCache(k: String): Any = {
    rowCache.get(k)
  }
}
