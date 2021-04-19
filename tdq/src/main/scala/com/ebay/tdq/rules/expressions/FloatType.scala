package com.ebay.tdq.rules.expressions

import scala.math.Numeric.FloatAsIfIntegral
import scala.math.{Fractional, Numeric, Ordering}
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class FloatType private() extends FractionalType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "FloatType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Float
  @transient lazy val tag = typeTag[InternalType]
  val numeric = implicitly[Numeric[Float]]
  val fractional = implicitly[Fractional[Float]]
  val ordering = new Ordering[Float] {
    override def compare(x: Float, y: Float): Int = Utils.nanSafeCompareFloats(x, y)
  }
  val asIntegral = FloatAsIfIntegral

  /**
   * The default size of a value of the FloatType is 4 bytes.
   */
  override def defaultSize: Int = 4

  protected override def asNullable: FloatType = this
}

case object FloatType extends FloatType
