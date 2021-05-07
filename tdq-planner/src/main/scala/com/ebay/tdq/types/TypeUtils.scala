package com.ebay.tdq.types

import com.ebay.tdq.expressions.TypeCheckResult

/**
 * @author juntzhang
 */
object TypeUtils {
  def checkForOrderingExpr(dt: DataType, caller: String): TypeCheckResult = {
    if (isOrderable(dt)) {
      TypeCheckResult.TypeCheckSuccess
    } else {
      TypeCheckResult.TypeCheckFailure(s"$caller does not support ordering on type $dt")
    }
  }

  /**
   * Returns true iff the data type can be ordered (i.e. can be sorted).
   */
  def isOrderable(dataType: DataType): Boolean = dataType match {
    case NullType => true
    case dt: AtomicType => true
    case _ => false
  }

  def compareBinary(x: Array[Byte], y: Array[Byte]): Int = {
    for (i <- 0 until x.length; if i < y.length) {
      val v1 = x(i) & 0xff
      val v2 = y(i) & 0xff
      val res = v1 - v2
      if (res != 0) return res
    }
    x.length - y.length
  }

  def getInterpretedOrdering(t: DataType): Ordering[Any] = {
    t match {
      case i: AtomicType => i.ordering.asInstanceOf[Ordering[Any]]
      case o => throw new IllegalStateException("Unexpected DataType: " + o)
    }
  }

  def getNumeric(t: DataType): Numeric[Any] =
    t.asInstanceOf[NumericType].numeric.asInstanceOf[Numeric[Any]]

  def checkForSameTypeInputExpr(types: Seq[DataType], caller: String): TypeCheckResult = {
    if (types.size <= 1) {
      TypeCheckResult.TypeCheckSuccess
    } else {
      val firstType = types.head
      types.foreach { t =>
        if (!t.sameType(firstType)) {
          return TypeCheckResult.TypeCheckFailure(
            s"input to $caller should all be the same type, but it's " +
              types.map(_.simpleString).mkString("[", ", ", "]"))
        }
      }
      TypeCheckResult.TypeCheckSuccess
    }
  }
}

