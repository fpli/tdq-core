package com.ebay.tdq.rules

import com.ebay.tdq.expressions._
import com.ebay.tdq.types.{LongType, StringType}
import com.google.common.base.Preconditions

/**
 * @author juntzhang
 */
object SojUdfRegistry extends DelegatingRegistry({
  case RegistryContext("SOJ_TAG", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojTag(
      subject = GetTdqEvent(Some("__TDQ_EVENT")),
      tag = operands.head.asInstanceOf[Literal].value.asInstanceOf[String],
      dataType = StringType,
      cacheKey = cacheKey
    )
  case RegistryContext("SOJ_NVL", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojNvl(
      subject = GetTdqEvent(Some("__TDQ_EVENT")),
      tag = operands.head.asInstanceOf[Literal].value.asInstanceOf[String],
      dataType = StringType,
      cacheKey = cacheKey
    )
  case cxt@RegistryContext("SOJ_PAGE_FAMILY", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojPageFamily(
      subject = operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey,
      cxt.tdqEnv
    )
  case RegistryContext("SOJ_TIMESTAMP", _, _) =>
    GetTdqField("soj_timestamp", LongType)
  case RegistryContext("SOJ_PARSE_RLOGID", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojParseRlogid(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_DECODE_BASE36_VEC", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    Base36Decoder(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_PARSE_CLIENTINFO", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    ClientInfoParser(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_DECODE_BASE64", operands: Array[Any], cacheKey) =>
    if (operands.length == 1) {
      EbayBase64(
        operands.head.asInstanceOf[Expression],
        Literal.apply(true),
        cacheKey
      )
    } else {
      Preconditions.checkArgument(operands.length == 2)
      EbayBase64(
        operands.head.asInstanceOf[Expression],
        operands(1).asInstanceOf[Expression],
        cacheKey
      )
    }
  case RegistryContext("SOJ_EXTRACT_FLAG", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    ExtractFlag(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GET_PAGE_TYPE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 6)
    GetPageType(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      operands(4).asInstanceOf[Expression],
      operands(5).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GET_URL_DOMAIN", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    GetURLDomain(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GET_URL_PARAMS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    GetUrlParams(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GET_URL_PATH", operands: Array[Any], cacheKey) =>
    if (operands.length == 1) {
      new GetURLPath(
        operands.head.asInstanceOf[Expression],
        cacheKey
      )
    } else {
      GetURLPath(
        operands.head.asInstanceOf[Expression],
        Some(operands(1).asInstanceOf[Expression]),
        cacheKey
      )
    }
  case RegistryContext("SOJ_GUID_TS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    GUID2DateHive(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GUID_IP", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    GUID2IPHive(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_GUID_SAMPLING", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    GuidSampling(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_IS_BIGINT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    IsBigInteger(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_IS_BITSET", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    IsBitSet(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("IS_DECIMAL", operands: Array[Any], cacheKey) =>
    if (operands.length == 1) {
      IsDecimal(
        operands.head.asInstanceOf[Expression],
        None,
        cacheKey
      )
    } else {
      IsDecimal(
        operands.head.asInstanceOf[Expression],
        Some(operands(1).asInstanceOf[Expression]),
        cacheKey
      )
    }
  case RegistryContext("SOJ_IS_DECIMAL", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojIsDecimal(
      operands.head.asInstanceOf[Expression],
      Some(operands(1).asInstanceOf[Expression]),
      Some(operands(2).asInstanceOf[Expression]),
      cacheKey
    )
  case RegistryContext("SOJ_IS_INTEGER", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    IsInteger(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_IS_VALIDIPV4", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    IsValidIPv4(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )
  case RegistryContext("SOJ_IS_VALIDPRIVATEIPV4", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    IsValidPrivateIPv4(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_SAMPLE_HASH", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SampleHash(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_BASE64TOBIGINT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SOJBase64ToLong(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_COLLAPSE_WHITESPACE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SOJCollapseWhiteSpace(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_EXTRACT_NVP", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 4)
    SojExtractNVP(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_GET_BASE64ENCODED_BITS_SET", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojGetBase64EncodedBitsSet(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_GET_BROWSER_TYPE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojGetBrowserType(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_GET_BROWSER_VERSION", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojGetBrowserVersion(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_GET_OS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojGetOs(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_JAVA_HASH", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 4)
    SojJavaHash(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_JSON_PARSE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojJsonParse(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_LIST_ELEMENT_COUNT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojListElementCount(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_STR_REVERSE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojStrReverse(
      operands.head.asInstanceOf[Expression],
      cacheKey
    )

  case RegistryContext("SOJ_LIST_GET_VAL_BY_IDX", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojListGetValByIdx(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_URL_DECODE_ESCAPES", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojURLDecodeEscape(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_TS_DATE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojTimestampToDate(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_TS_MILS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojTimestampToDateWithMilliSecond(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("STRING_TS_TO_SOJTS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    StringTSToSojTS(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("STRING_DATE_TO_SOJTS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    StringDateToSojTS(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("DECODE_PLMT_TAG", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    DecodePlmtTag(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_GET_TOP_DOMAIN", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojGetTopDomain(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_URL_EXTRACT_NVP", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojUrlExtractNvp(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_STR_BETWEEN_STR", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    StrBetweenStr(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_LIST_GET_RANGE_BY_IDX", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 4)
    SojListGetRangeByIndex(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_LIST_LAST_ELEMENT", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojListLastElement(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_MD5_HASH_32", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 4)
    SojMd5Hash32(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_MD5_HASH_128", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 4)
    SojMd5Hash128(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      operands(3).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_REPLACE_CHAR", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojReplaceChar(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_REPLACE_RCHAR", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojReplaceRChar(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_STR2DATE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojStr2Date(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_TS_TO_TD", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojStr2DateTimeStamp(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_NVL2", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojTagFetcher(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_TIMESTAMP2DATE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojTimestampToDateWithMilliSecond(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_VALUE_IN_LIST", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    SojValueInList(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_STR_BETWEEN_ENDLIST", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    StrBetweenEndList(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_CSTR_STRCHR", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    CstrStrchr(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_IS_TIMESTAMP", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    IsTimestamp(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_GET_UA_VERSION", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojGetUaVersion(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("SOJ_STR_BETWEEN", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    StrBetween(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )
  case RegistryContext("SOJ_STR_BETWEEN_LIST", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    StrBetweenList(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_UDF_SOJ_GET_B64_BIT_POSTNS", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    UdfSojGetB64BitPostns(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_DECODESRPITMCARDSIG", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 3)
    DecodeSRPItmcardSig(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      operands(2).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_PARSER", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojFunctionOneDecode(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_PARSER_NO_DECODE", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojFunctionNoDecode(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_STR_TO_MAP", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 2)
    SojStringToMap(
      operands.head.asInstanceOf[Expression],
      operands(1).asInstanceOf[Expression],
      cacheKey = cacheKey
    )

  case RegistryContext("SOJ_MAP_TO_STR", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    SojMapToStr(
      operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey
    )

})
