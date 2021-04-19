package com.ebay.tdq.rules.expressions

import java.util.Locale

/**
 * @author juntzhang
 */
abstract class DataType extends AbstractDataType {
  /**
   * The default size of a value of this data type, used internally for size estimation.
   */
  def defaultSize: Int

  override def defaultConcreteType: DataType = this

  def caseSensitiveAnalysis = false

  /** String representation for the type saved in external catalogs. */
  def catalogString: String = simpleString

  def simpleString(maxNumberFields: Int): String = simpleString

  override def acceptsType(other: DataType): Boolean = sameType(other)

  def sameType(other: DataType): Boolean = {
    this == other
  }

  protected def asNullable: DataType

  def sql: String = simpleString.toUpperCase(Locale.ROOT)

  def simpleString: String = typeName

  def typeName: String = {
    this.getClass.getSimpleName
      .stripSuffix("$").stripSuffix("Type").stripSuffix("UDT")
      .toLowerCase(Locale.ROOT)
  }
}

