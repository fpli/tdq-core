package com.ebay.tdq.rules.expressions

import scala.math.Ordering
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class BooleanType private() extends AtomicType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "BooleanType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Boolean
  @transient lazy val tag = typeTag[InternalType]
  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the BooleanType is 1 byte.
   */
  override def defaultSize: Int = 1

  protected override def asNullable: BooleanType = this
}

case object BooleanType extends BooleanType

