package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.planner.LkpManager
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
    val e = input.getCache("__TDQ_EVENT")
    if (e == null) {
      // restore from pronto, hard code
      if (input.default && dataType.isInstanceOf[DoubleType]) {
        return 0d
      }
      return null
    }
    e.asInstanceOf[TdqEvent].get(name)
  }
}

case class GetTdqEvent(cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = ObjectType(classOf[TdqEvent])

  protected override def eval(input: InternalRow): Any = {
    input.getCache("__TDQ_EVENT")
  }
}

case class IsBBWOAPageWithItm(subject: Expression, cacheKey: Option[String] = None, tdqEnv: TdqEnv) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = BooleanType

  protected override def eval(input: InternalRow): Any = {
    LkpManager.getInstance(tdqEnv).isBBWOAPagesWithItm(subject.call(input).asInstanceOf[Int])
  }
}
