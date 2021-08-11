package com.ebay.tdq.rules

import com.ebay.tdq.expressions._
import com.ebay.tdq.types.{LongType, TimestampType}
import com.google.common.base.Preconditions

/**
 * @author juntzhang
 */
object TdqUdfRegistry extends DelegatingRegistry({
  case cxt@RegistryContext("IS_BBWOA_PAGE_WITH_ITM", operands: Array[Any], cacheKey) =>
    Preconditions.checkArgument(operands.length == 1)
    IsBBWOAPageWithItm(
      subject = operands.head.asInstanceOf[Expression],
      cacheKey = cacheKey,
      cxt.tdqEnv
    )
  case RegistryContext("EVENT_TIMESTAMP", _, _) =>
    GetTdqField("event_timestamp", TimestampType)
  case RegistryContext("EVENT_TIME_MILLIS", _, _) =>
    GetTdqField("event_time_millis", LongType)
  case RegistryContext("CURRENT_TIMESTAMP", operands, cacheKey) =>
    CurrentTimestamp()
  case RegistryContext("UNIX_TIMESTAMP", operands: Array[Any], cacheKey) =>
    if (operands.length == 0) {
      new UnixTimestamp(cacheKey)
    } else if (operands.length == 1) {
      new UnixTimestamp(operands.head.asInstanceOf[Expression], cacheKey)
    } else if (operands.length == 2) {
      new UnixTimestamp(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], cacheKey)
    } else {
      UnixTimestamp(operands.head.asInstanceOf[Expression], operands(1).asInstanceOf[Expression], Some(operands(2).asInstanceOf[String]), cacheKey)
    }
  case RegistryContext("TO_TIMESTAMP", operands: Array[Any], cacheKey) =>
    val left = operands.head.asInstanceOf[Expression]
    if (operands.length == 1) {
      new ParseToTimestamp(left, cacheKey)
    } else if (operands.length == 2) {
      new ParseToTimestamp(left, operands(1).asInstanceOf[Expression], cacheKey)
    } else {
      ParseToTimestamp(left, Option(operands(1).asInstanceOf[Expression]), operands(2).asInstanceOf[Expression], cacheKey)
    }
  case cxt@RegistryContext("ITEM", operands: Array[Any], cacheKey) =>
    val names = operands.flatMap {
      case field: GetTdqField =>
        Seq(field.name)
      case literal: Literal =>
        Seq(literal.value.toString)
      case field0: GetTdqField0 =>
        field0.names
      case t =>
        throw new IllegalStateException("Unexpected ITEM: " + t)
    }
    GetTdqField0(names, cxt.getDataType(names))
})
