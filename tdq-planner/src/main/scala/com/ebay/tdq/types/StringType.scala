package com.ebay.tdq.types

import scala.math.Ordering
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class StringType private() extends AtomicType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "StringType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = String
  @transient lazy val tag = typeTag[InternalType]
  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the StringType is 20 bytes.
   */
  override def defaultSize: Int = 20

  def asNullable: StringType = this
}

case object StringType extends StringType
