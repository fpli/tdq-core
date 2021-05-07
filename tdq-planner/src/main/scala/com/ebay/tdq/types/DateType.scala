package com.ebay.tdq.types

import scala.math.Ordering
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class DateType private() extends AtomicType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "DateType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Int

  @transient lazy val tag = typeTag[InternalType]

  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the DateType is 4 bytes.
   */
  override def defaultSize: Int = 4

  protected override def asNullable: DateType = this
}

case object DateType extends DateType
