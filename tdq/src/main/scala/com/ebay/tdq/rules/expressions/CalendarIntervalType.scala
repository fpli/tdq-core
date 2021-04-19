package com.ebay.tdq.rules.expressions

/**
 * @author juntzhang
 */
class CalendarIntervalType private() extends DataType {

  override def defaultSize: Int = 16

  override def asNullable: CalendarIntervalType = this
}

case object CalendarIntervalType extends CalendarIntervalType
