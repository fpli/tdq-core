package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.planner.LkpManager
import com.ebay.tdq.planner.utils._
import com.ebay.tdq.types._
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
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

case class SojPageFamily(subject: Expression, cacheKey: Option[String] = None, tdqEnv: TdqEnv) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = StringType

  protected override def eval(input: InternalRow): Any = {
    LkpManager.getInstance(tdqEnv).getPageFmlyByPageId(subject.call(input).asInstanceOf[Int])
  }
}

case class SojURLDecodeEscape(url: Expression, enc: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SOJURLDecodeEscapeHive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(url.call(input).asInstanceOf[String], enc.call(input).asInstanceOf[String])
  }
}

case class SojParseRlogid(subject: Expression, infoType: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojParseRlogid()

  protected override def eval(input: InternalRow): Any = {
    hive
      .evaluate(subject.call(input).asInstanceOf[String], infoType.call(input).asInstanceOf[String])
  }
}

case class SojListGetValByIdx(
  arg1: Expression,
  arg2: Expression,
  arg3: Expression,
  cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojListGetValByIdx()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[Int]
    )
  }
}

case class SojGetURLPath(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GetURLPath()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(subject.call(input).asInstanceOf[String])
  }
}

case class Base36Decoder(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.Base36Decoder()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(subject.call(input).asInstanceOf[String])
  }
}

case class ClientInfoParser(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.ClientInfoParser()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[String])
  }
}

case class EbayBase64(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.EbayBase64()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Boolean])
  }
}

case class ExtractFlag(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.ExtractFlag()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Int])
  }
}

case class GetPageType(
  arg1: Expression,
  arg2: Expression,
  arg3: Expression,
  arg4: Expression,
  arg5: Expression,
  arg6: Expression,
  cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GetPageType()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[Int],
      arg4.call(input).asInstanceOf[Int],
      arg5.call(input).asInstanceOf[Int],
      arg6.call(input).asInstanceOf[Int]
    )
  }
}

case class GetURLDomain(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GetURLDomain()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class GetUrlParams(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GetUrlParams()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class GetURLPath(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GetURLPath()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class GUID2DateHive(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GUID2DateHive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class GUID2IPHive(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GUID2IPHive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class GuidSampling(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: BooleanType.type = BooleanType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.GuidSampling()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Int])
  }
}

case class IsBigInteger(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsBigInteger()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class IsBitSet(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: BooleanType.type = BooleanType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsBitSet()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Int])
  }
}

case class IsDecimal(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsDecimal()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Int], arg3.call(input).asInstanceOf[Int])
  }
}

case class IsInteger(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsInteger()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class IsValidIPv4(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsValidIPv4()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class IsValidPrivateIPv4(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: BooleanType.type = BooleanType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsValidPrivateIPv4Hive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SampleHash(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SampleHash()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[Int])
  }
}

case class SOJBase64ToLong(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: LongType.type = LongType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SOJBase64ToLongHive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SOJCollapseWhiteSpace(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SOJCollapseWhiteSpaceHive()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojExtractNVP(arg1: Expression, arg2: Expression, arg3: Expression, arg4: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojExtractNVP()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[String], arg3.call(input).asInstanceOf[String], arg4.call(input).asInstanceOf[String])
  }
}

case class SojGetBase64EncodedBitsSet(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetBase64EncodedBitsSet()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}


case class SojGetBrowserType(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetBrowserType()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojGetBrowserVersion(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetBrowserVersion()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojGetOs(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetOs()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojJavaHash(arg1: Expression, arg2: Expression, arg3: Expression, arg4: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojJavaHash()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[String], arg3.call(input).asInstanceOf[String], arg4.call(input).asInstanceOf[Int])
  }
}

case class SojJsonParse(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojJsonParse()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[String])
  }
}

case class SojListElementCount(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojListElementCount()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String], arg2.call(input).asInstanceOf[String])
  }
}

case class SojStrReverse(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojStrReverse()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojTimestampToDate(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojTimestampToDate()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[Long])
  }
}

case class SojTimestampToDateWithMilliSecond(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojTimestampToDateWithMilliSecond()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[Long])
  }
}

case class StringTSToSojTS(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: LongType.type = LongType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StringTSToSojTS()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class StringDateToSojTS(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: LongType.type = LongType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StringDateToSojTS()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class DecodePlmtTag(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.DecodePlmtTag()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojGetTopDomain(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetTopDomain()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(arg1.call(input).asInstanceOf[String])
  }
}

case class SojUrlExtractNvp(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojUrlExtractNvp()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[Int]
    )
  }
}

case class StrBetweenStr(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StrBetweenStr()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class SojListGetRangeByIndex(arg1: Expression, arg2: Expression, arg3: Expression, arg4: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojListGetRangeByIndex()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[Int],
      arg4.call(input).asInstanceOf[Int]
    )
  }
}

case class SojListLastElement(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojListLastElement()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class SojMd5Hash32(arg1: Expression, arg2: Expression, arg3: Expression, arg4: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojMd5Hash32()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String],
      arg4.call(input).asInstanceOf[Int]
    )
  }
}

case class SojMd5Hash128(arg1: Expression, arg2: Expression, arg3: Expression, arg4: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojMd5Hash128()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String],
      arg4.call(input).asInstanceOf[Int]
    )
  }
}

case class SojReplaceChar(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojReplaceChar()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class SojReplaceRChar(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojReplaceRChar()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class SojStr2Date(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojStr2Date()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String]
    )
  }
}

case class SojStr2DateTimeStamp(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojStr2DateTimeStamp()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String]
    )
  }
}

case class SojTagFetcher(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojTagFetcher()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class SojValueInList(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojValueInList()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class StrBetweenEndList(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StrBetweenEndList()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class CstrStrchr(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.CstrStrchr()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class IsTimestamp(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.IsTimestamp()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[Int]
    )
  }
}

case class SojGetUaVersion(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojGetUaVersion()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[Int]
    )
  }
}

case class StrBetween(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StrBetween()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String].charAt(0),
      arg3.call(input).asInstanceOf[String].charAt(0)
    )
  }
}

case class StrBetweenList(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.StrBetweenList()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String],
      arg3.call(input).asInstanceOf[String]
    )
  }
}

case class UdfSojGetB64BitPostns(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType.type = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.UdfSojGetB64BitPostns()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[Int]
    )
  }
}

case class DecodeSRPItmcardSig(arg1: Expression, arg2: Expression, arg3: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: IntegerType.type = IntegerType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.DecodeSRPItmcardSig()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[Int],
      arg3.call(input).asInstanceOf[Int]
    )
  }
}

case class SojFunctionOneDecode(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: MapType = MapType(StringType, StringType)

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojFunctionOneDecode()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class SojFunctionNoDecode(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: MapType = MapType(StringType, StringType)

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojFunctionNoDecode()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class SojStringToMap(arg1: Expression, arg2: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: MapType = MapType(IntegerType, StringType)

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojStringToMap()

  protected override def eval(input: InternalRow): Any = {
    hive.evaluate(
      arg1.call(input).asInstanceOf[String],
      arg2.call(input).asInstanceOf[String]
    )
  }
}

case class SojMapToStr(arg1: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType: StringType = StringType

  override def nullable: Boolean = true

  lazy val hive = new udf.soj.SojMapToStr()

  protected override def eval(input: InternalRow): Any = {
    val v = arg1.call(input)
    if (v == null) {
      return null
    }
    hive.evaluate(v.asInstanceOf[java.util.Map[String, String]])
  }
}
