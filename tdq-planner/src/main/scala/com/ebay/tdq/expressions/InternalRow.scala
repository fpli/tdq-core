package com.ebay.tdq.expressions

import java.util.{Map => JMap}

/**
 * @author juntzhang
 */
case class InternalRow(operands: Array[Any], rowCache: JMap[String, Any], default: Boolean = false) {

  def containsKey(k: Option[String]): Boolean = {
    k.isDefined && k.get.nonEmpty && rowCache.containsKey(k.get)
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
