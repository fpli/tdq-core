package com.ebay.tdq.types

import scala.math.{Integral, Numeric, Ordering}
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class IntegerType private() extends IntegralType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "IntegerType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Int
  @transient lazy val tag = typeTag[InternalType]
  val numeric = implicitly[Numeric[Int]]
  val integral = implicitly[Integral[Int]]
  val ordering = implicitly[Ordering[InternalType]]

  /**
   * The default size of a value of the IntegerType is 4 bytes.
   */
  override def defaultSize: Int = 4

  override def simpleString: String = "int"

  protected override def asNullable: IntegerType = this
}

case object IntegerType extends IntegerType
