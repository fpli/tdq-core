package com.ebay.tdq.types

import com.ebay.tdq.expressions.Utils

import scala.math.Numeric.DoubleAsIfIntegral
import scala.math.{Fractional, Numeric, Ordering}
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class DoubleType private() extends FractionalType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "DoubleType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Double
  @transient lazy val tag = typeTag[InternalType]
  val numeric = implicitly[Numeric[Double]]
  val fractional = implicitly[Fractional[Double]]
  val ordering = new Ordering[Double] {
    override def compare(x: Double, y: Double): Int = Utils.nanSafeCompareDoubles(x, y)
  }
  val asIntegral = DoubleAsIfIntegral


  /**
   * The default size of a value of the DoubleType is 8 bytes.
   */
  override def defaultSize: Int = 8

  def asNullable: DoubleType = this
}

case object DoubleType extends DoubleType