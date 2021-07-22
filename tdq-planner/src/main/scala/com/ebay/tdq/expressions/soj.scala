package com.ebay.tdq.expressions

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.types.{BooleanType, DataType, StringType}
import com.ebay.tdq.utils.LkpManager
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
case class SojParseRlogid(subject: Expression, infoType: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType = StringType

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = new com.ebay.sojourner.common.util.SojParseRlogid()
    .evaluate(subject.call(input).asInstanceOf[String], infoType.call(input).asInstanceOf[String])
}

object SojTag {
  val SPLIT_DEL = "\\|"

  def eval(rawEvent: RawEvent, tag: String): String = {
    val tags = tag.split(SPLIT_DEL)
    for (t <- tags) {
      if (StringUtils.isNotBlank(rawEvent.getSojA.get(t))) return rawEvent.getSojA.get(t)
      if (StringUtils.isNotBlank(rawEvent.getSojC.get(t))) return rawEvent.getSojC.get(t)
      if (StringUtils.isNotBlank(rawEvent.getSojK.get(t))) return rawEvent.getSojK.get(t)
    }
    null
  }
}

case class SojTag(subject: GetRawEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val rawEvent = subject.call(input).asInstanceOf[RawEvent]
    SojTag.eval(rawEvent, tag)
  }
}

case class SojNvl(subject: GetRawEvent, tag: String,
                  dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val rawEvent = subject.call(input).asInstanceOf[RawEvent]
    val v = SojTag.eval(rawEvent, tag)
    if (StringUtils.isNotBlank(v)) {
      v
    } else {
      val tags = tag.split(SojTag.SPLIT_DEL)
      for (t <- tags) {
        if (!ClientData.FIELDS.contains(t)) {
          return null
        }
        val v = PropertyUtils.getProperty(input.getCache("__RAW_EVENT"), s"clientData.$t")
        if (v != null) {
          return v
        }
      }
      null
    }
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