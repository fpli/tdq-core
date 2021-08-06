package com.ebay.tdq.types

import scala.math.{Integral, Numeric, Ordering}
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class ShortType private() extends IntegralType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "ShortType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Short
  @transient lazy val tag = typeTag[InternalType]
  val numeric = implicitly[Numeric[Short]]
  val integral = implicitly[Integral[Short]]
  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the ShortType is 2 bytes.
   */
  override def defaultSize: Int = 2

  override def simpleString: String = "smallint"

  def asNullable: ShortType = this
}

case object ShortType extends ShortType
