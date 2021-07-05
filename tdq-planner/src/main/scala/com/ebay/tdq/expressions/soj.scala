package com.ebay.tdq.expressions

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.common.util.SojUtils
import com.ebay.tdq.types.{DataType, StringType}

/**
 * @author juntzhang
 */
case class SojParseRlogid(subject: Expression, infoType: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType = StringType

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = new com.ebay.sojourner.common.util.SojParseRlogid()
    .evaluate(subject.call(input).asInstanceOf[String], infoType.call(input).asInstanceOf[String])
}

case class SojNvl(subject: GetRawEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = SojUtils.getTagValueStr(
    subject.call(input).asInstanceOf[RawEvent], tag
  )
}

case class SojPageFamily(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = StringType

  protected override def eval(input: InternalRow): Any = {
    SojUtils.getPageFmly(subject.call(input).asInstanceOf[Int])
  }
}