package com.ebay.tdq.expressions

import java.util.TimeZone

/**
 * @author juntzhang
 */
/**
 * Common base class for time zone aware expressions.
 */
trait TimeZoneAwareExpression extends Expression {
  @transient lazy val timeZone: TimeZone = DateTimeUtils.getTimeZone(timeZoneId.get)

  /** the timezone ID to be used to evaluate value. */
  def timeZoneId: Option[String]

  /** Returns a copy of this expression with the specified timeZoneId. */
  def withTimeZone(timeZoneId: String): TimeZoneAwareExpression
}