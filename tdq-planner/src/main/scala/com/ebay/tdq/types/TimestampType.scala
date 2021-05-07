package com.ebay.tdq.types

import scala.math.Ordering
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class TimestampType private() extends AtomicType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "TimestampType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Long

  @transient lazy val tag = typeTag[InternalType]

  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the TimestampType is 8 bytes.
   */
  override def defaultSize: Int = 8

  protected override def asNullable: TimestampType = this
}

case object TimestampType extends TimestampType
