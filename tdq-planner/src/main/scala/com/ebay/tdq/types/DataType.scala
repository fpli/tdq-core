package com.ebay.tdq.types

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

  def simpleString: String = typeName

  def typeName: String = {
    this.getClass.getSimpleName
      .stripSuffix("$").stripSuffix("Type").stripSuffix("UDT")
      .toLowerCase(Locale.ROOT)
  }

  def simpleString(maxNumberFields: Int): String = simpleString

  override def acceptsType(other: DataType): Boolean = sameType(other)

  def sameType(other: DataType): Boolean = {
    this == other
  }

  def sql: String = simpleString.toUpperCase(Locale.ROOT)

  def asNullable: DataType
}

object DataType {
  private val nonDecimalNameToType = {
    Seq(NullType, DateType, TimestampType, BinaryType, IntegerType, BooleanType, LongType,
      DoubleType, FloatType, ShortType, ByteType, StringType, CalendarIntervalType)
      .map(t => t.typeName -> t).toMap
  }

  /** Given the string representation of a type, return its DataType */
  def nameToType(name: String): DataType = {
    val FIXED_DECIMAL = """decimal\(\s*(\d+)\s*,\s*(\-?\d+)\s*\)""".r
    name.toLowerCase() match {
      case "decimal" => DecimalType.USER_DEFAULT
      case FIXED_DECIMAL(precision, scale) => DecimalType(precision.toInt, scale.toInt)
      case other => nonDecimalNameToType.getOrElse(
        other,
        throw new IllegalArgumentException(
          s"Failed to convert the JSON string '$name' to a data type."))
    }
  }

}

