package com.ebay.tdq.expressions

import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
case class TdqTimestamp(name: String = "event_time_millis", dataType: DataType = LongType) extends LeafExpression {
  val cacheKey: Option[String] = None

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val o = input.getCache(name)
    if (o != null) {
      return o
    }
    input.getCache("__TDQ_EVENT").asInstanceOf[TdqEvent].get(name)
  }
}

// todo get type from schema
case class GetStructField(name: String, dataType: DataType = StringType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val o = input.getCache(name)
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
