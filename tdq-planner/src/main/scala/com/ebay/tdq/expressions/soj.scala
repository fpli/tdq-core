package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.planner.LkpManager
import com.ebay.tdq.types.{BooleanType, DataType, StringType}
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
case class SojParseRlogid(subject: Expression, infoType: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = new com.ebay.sojourner.common.util.SojParseRlogid()
    .evaluate(subject.call(input).asInstanceOf[String], infoType.call(input).asInstanceOf[String])
}

object SojTag {
  type JMap = java.util.Map[String, String]
  val SPLIT_DEL = "\\|"

  def evalTag(tdqEvent: TdqEvent, tag: String): String = {
    val tags = tag.split(SPLIT_DEL)
    for (t <- tags) {
      val v = eval(tdqEvent, t)
      if (StringUtils.isNotBlank(v)) return v
    }
    null
  }

  def evalAll(tdqEvent: TdqEvent, tag: String): String = {
    val tags = tag.split(SPLIT_DEL)
    for (t <- tags) {
      var v = eval(tdqEvent, t)
      if (StringUtils.isNotBlank(v)) return v
      v = tdqEvent.get("clientData").asInstanceOf[JMap].get(t)
      if (StringUtils.isNotBlank(v)) return v
    }
    null
  }

  def eval(tdqEvent: TdqEvent, t: String): String = {
    var v = tdqEvent.get("sojA").asInstanceOf[JMap].get(t)
    if (StringUtils.isNotBlank(v)) return v
    v = tdqEvent.get("sojC").asInstanceOf[JMap].get(t)
    if (StringUtils.isNotBlank(v)) return v
    v = tdqEvent.get("sojK").asInstanceOf[JMap].get(t)
    if (StringUtils.isNotBlank(v)) return v
    null
  }
}

case class SojTag(subject: GetTdqEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val tdqEvent = subject.call(input).asInstanceOf[TdqEvent]
    SojTag.evalTag(tdqEvent, tag)
  }
}

case class SojNvl(subject: GetTdqEvent, tag: String,
  dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val tdqEvent = subject.call(input).asInstanceOf[TdqEvent]
    SojTag.evalAll(tdqEvent, tag)
  }
}

case class SojPageFamily(subject: Expression, cacheKey: Option[String] = None, jdbcEnv: JdbcEnv) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = StringType

  protected override def eval(input: InternalRow): Any = {
    LkpManager.getInstance(jdbcEnv).getPageFmlyByPageId(subject.call(input).asInstanceOf[Int])
  }
}

case class IsBBWOAPageWithItm(subject: Expression, cacheKey: Option[String] = None, jdbcEnv: JdbcEnv) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = BooleanType

  protected override def eval(input: InternalRow): Any = {
    LkpManager.getInstance(jdbcEnv).isBBWOAPagesWithItm(subject.call(input).asInstanceOf[Int])
  }
}