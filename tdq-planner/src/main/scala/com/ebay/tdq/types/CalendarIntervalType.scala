package com.ebay.tdq.types

/**
 * @author juntzhang
 */
class CalendarIntervalType private() extends DataType {

  override def defaultSize: Int = 16

  override def asNullable: CalendarIntervalType = this
}

case object CalendarIntervalType extends CalendarIntervalType
