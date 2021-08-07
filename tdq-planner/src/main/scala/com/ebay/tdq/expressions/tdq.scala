package com.ebay.tdq.expressions

import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.types._

/**
 * example: payload['annotation.nId']
 *
 * @param names ["payload", "annotation.nId"]
 */
case class GetTdqField0(names: Array[String], dataType: DataType = StringType) extends LeafExpression {
  override def nullable: Boolean = true

  def cacheKey: Option[String] = Some(names.mkString("\u0001"))

  protected override def eval(input: InternalRow): Any = {
    val v = input.getCache(cacheKey.get)
    if (v != null) {
      return v
    }
    input.getCache("__TDQ_EVENT").asInstanceOf[TdqEvent].get(names)
  }

  override def toString: String = s"GetTdqField0(${names.mkString("|")},$dataType)"
}

/**
 * @param name clientData.remoteIP split by '.'
 * @author juntzhang
 */
case class GetTdqField(name: String, dataType: DataType = StringType) extends LeafExpression {
  def cacheKey: Option[String] = Some(name)

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val o = input.getCache(cacheKey.get)
    if (o != null) {
      return o
    }
    input.getCache("__TDQ_EVENT").asInstanceOf[TdqEvent].get(name)
  }
}

case class GetTdqEvent(cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = ObjectType(classOf[TdqEvent])

  protected override def eval(input: InternalRow): Any = {
    input.getCache("__TDQ_EVENT")
  }
}

case class DebugEvent(name: String) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = BooleanType

  def cacheKey: Option[String] = None

  protected override def eval(input: InternalRow): Any = {
    true
  }
}
