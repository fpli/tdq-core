package com.ebay.tdq.types

/**
 * Companion object for ArrayType.
 *
 * @since 1.3.0
 */
object ArrayType extends AbstractDataType {
  /**
   * Construct a [[ArrayType]] object with the given element type. The `containsNull` is true.
   */
  def apply(elementType: DataType): ArrayType = ArrayType(elementType, containsNull = true)

  override def defaultConcreteType: DataType = ArrayType(NullType, containsNull = true)

  override def acceptsType(other: DataType): Boolean = {
    other.isInstanceOf[ArrayType]
  }

  override def simpleString: String = "array"
}

/**
 * The data type for collections of multiple values.
 * Internally these are represented as columns that contain a ``scala.collection.Seq``.
 *
 * Please use `DataTypes.createArrayType()` to create a specific instance.
 *
 * An [[ArrayType]] object comprises two fields, `elementType: [[DataType]]` and
 * `containsNull: Boolean`. The field of `elementType` is used to specify the type of
 * array elements. The field of `containsNull` is used to specify if the array has `null` values.
 *
 * @param elementType  The data type of values.
 * @param containsNull Indicates if values have `null` values
 * @since 1.3.0
 */
case class ArrayType(elementType: DataType, containsNull: Boolean) extends DataType {

  /** No-arg constructor for kryo. */
  protected def this() = this(null, false)

  def buildFormattedString(prefix: String, builder: StringBuilder): Unit = {
    builder.append(
      s"$prefix-- element: ${elementType.typeName} (containsNull = $containsNull)\n")
    DataType.buildFormattedString(elementType, s"$prefix    |", builder)
  }

  /**
   * The default size of a value of the ArrayType is the default size of the element type.
   * We assume that there is only 1 element on average in an array. See SPARK-18853.
   */
  override def defaultSize: Int = 1 * elementType.defaultSize

  override def simpleString: String = s"array<${elementType.simpleString}>"

  override def catalogString: String = s"array<${elementType.catalogString}>"

  override def sql: String = s"ARRAY<${elementType.sql}>"

  override def asNullable: ArrayType =
    ArrayType(elementType.asNullable, containsNull = true)
}
