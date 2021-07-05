package com.ebay.tdq.expressions

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.types._
import org.apache.commons.beanutils.PropertyUtils

/**
 * @author juntzhang
 */
case class TdqTimestamp(name: String = "event_time_millis", dataType: DataType = LongType) extends LeafExpression {
  val cacheKey = None

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val o = input.getCache(name)
    if (o != null) {
      return o
    }
    input.getCache("__RAW_EVENT").asInstanceOf[RawEvent].getEventTimestamp
  }
}

case class GetStructField(name: String, dataType: DataType = StringType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val o = input.getCache(name)
    if (o != null) {
      return o
    }
    PropertyUtils.getProperty(input.getCache("__RAW_EVENT"), name)
  }
}

case class GetRawEvent(cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = ObjectType(classOf[RawEvent])

  protected override def eval(input: InternalRow): Any = {
    input.getCache("__RAW_EVENT")
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
