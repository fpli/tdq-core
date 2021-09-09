/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ebay.tdq.types

/**
 * The data type for Maps. Keys in a map are not allowed to have `null` values.
 *
 * Please use `DataTypes.createMapType()` to create a specific instance.
 *
 * @param keyType           The data type of map keys.
 * @param valueType         The data type of map values.
 * @param valueContainsNull Indicates if map values have `null` values.
 */
case class MapType(
  keyType: DataType,
  valueType: DataType,
  valueContainsNull: Boolean) extends DataType {

  /** No-arg constructor for kryo. */
  def this() = this(null, null, false)

  /**
   * The default size of a value of the MapType is
   * (the default size of the key type + the default size of the value type).
   * We assume that there is only 1 element on average in a map. See SPARK-18853.
   */
  override def defaultSize: Int = 1 * (keyType.defaultSize + valueType.defaultSize)

  override def simpleString: String = s"map<${keyType.simpleString},${valueType.simpleString}>"

  override def catalogString: String = s"map<${keyType.catalogString},${valueType.catalogString}>"

  override def sql: String = s"MAP<${keyType.sql}, ${valueType.sql}>"

  def asNullable: MapType =
    MapType(keyType.asNullable, valueType.asNullable, valueContainsNull = true)

  def buildFormattedString(prefix: String, builder: StringBuilder): Unit = {
    builder.append(s"$prefix-- key: ${keyType.typeName}\n")
    builder.append(s"$prefix-- value: ${valueType.typeName} " +
      s"(valueContainsNull = $valueContainsNull)\n")
    DataType.buildFormattedString(keyType, s"$prefix    |", builder)
    DataType.buildFormattedString(valueType, s"$prefix    |", builder)
  }
}

/**
 * @since 1.3.0
 */
object MapType extends AbstractDataType {

  override def defaultConcreteType: DataType = apply(NullType, NullType)

  override def acceptsType(other: DataType): Boolean = {
    other.isInstanceOf[MapType]
  }

  override def simpleString: String = "map"

  /**
   * Construct a [[MapType]] object with the given key type and value type.
   * The `valueContainsNull` is true.
   */
  def apply(keyType: DataType, valueType: DataType): MapType =
    MapType(keyType: DataType, valueType: DataType, valueContainsNull = true)
}
