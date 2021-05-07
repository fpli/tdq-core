package com.ebay.tdq.types

import scala.math.{Integral, Numeric, Ordering}
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class ByteType private() extends IntegralType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "ByteType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Byte
  @transient lazy val tag = typeTag[InternalType]
  val numeric = implicitly[Numeric[Byte]]
  val integral = implicitly[Integral[Byte]]
  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the ByteType is 1 byte.
   */
  override def defaultSize: Int = 1

  override def simpleString: String = "tinyint"

  protected override def asNullable: ByteType = this
}

case object ByteType extends ByteType

