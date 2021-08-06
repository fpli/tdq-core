package com.ebay.tdq.types

import scala.math.Ordering
import scala.reflect.runtime.universe.typeTag

/**
 * @author juntzhang
 */
class BinaryType extends AtomicType {
  // The companion object and this class is separated so the companion object also subclasses
  // this type. Otherwise, the companion object would be of type "BinaryType$" in byte code.
  // Defined with a private constructor so the companion object is the only possible instantiation.
  type InternalType = Array[Byte]

  @transient lazy val tag = typeTag[InternalType]

  val ordering = new Ordering[InternalType] {
    def compare(x: Array[Byte], y: Array[Byte]): Int = {
      TypeUtils.compareBinary(x, y)
    }
  }

  /**
   * The default size of a value of the BinaryType is 100 bytes.
   */
  override def defaultSize: Int = 100

  def asNullable: BinaryType = this
}

case object BinaryType extends BinaryType
