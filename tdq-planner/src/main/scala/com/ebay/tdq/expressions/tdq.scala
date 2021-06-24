package com.ebay.tdq.expressions

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.common.util.SojUtils
import com.ebay.tdq.types._
import org.apache.commons.beanutils.PropertyUtils

/**
 * @author juntzhang
 */
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

case class ExtractTag(subject: GetRawEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = SojUtils.getTagValueStr(
    subject.call(input).asInstanceOf[RawEvent], tag
  )
}

case class GetRawEvent(cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = ObjectType(classOf[RawEvent])

  protected override def eval(input: InternalRow): Any = {
    input.getCache("__RAW_EVENT")
  }
}

case class PageFamily(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = StringType

  protected override def eval(input: InternalRow): Any = {
    SojUtils.getPageFmly(subject.call(input).asInstanceOf[Int])
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
