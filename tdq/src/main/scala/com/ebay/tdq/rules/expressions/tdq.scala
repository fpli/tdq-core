package com.ebay.tdq.rules.expressions

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.common.util.SojUtils
import org.apache.commons.beanutils.PropertyUtils

/**
 * @author juntzhang
 */
case class GetStructField(name: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    val o = input.cachedData.get(name)
    if (o != null) {
      return o
    }
    PropertyUtils.getProperty(input.cachedData.get("__RAW_EVENT"), name)
  }

  override def nullable: Boolean = true
}

case class ExtractTag(subject: GetRawEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow, fromCache: Boolean): Any = SojUtils.getTagValueStr(
    subject.call(input, fromCache).asInstanceOf[RawEvent], tag
  )
}

case class GetRawEvent(cacheKey: Option[String] = None) extends LeafExpression {
  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    input.cachedData.get("__RAW_EVENT")
  }

  override def nullable: Boolean = true

  override def dataType: DataType = ObjectType(classOf[RawEvent])
}

case class DebugEvent(name: String) extends LeafExpression {
  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    true
  }

  override def nullable: Boolean = true

  override def dataType: DataType = BooleanType

  def cacheKey: Option[String] = None
}
