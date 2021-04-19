package com.ebay.tdq.rules.expressions

/**
 * @author juntzhang
 */
/**
 * Represents a JVM object that is passing through Spark SQL expression evaluation.
 */
case class ObjectType(cls: Class[_]) extends DataType {
  override def defaultSize: Int = 4096

  def asNullable: DataType = this

  override def simpleString: String = cls.getName

  override def acceptsType(other: DataType): Boolean = other match {
    case ObjectType(otherCls) => cls.isAssignableFrom(otherCls)
    case _ => false
  }
}

object ObjectType extends AbstractDataType {
  override def defaultConcreteType: DataType =
    throw new UnsupportedOperationException("null literals can't be casted to ObjectType")

  override def acceptsType(other: DataType): Boolean = other match {
    case ObjectType(_) => true
    case _ => false
  }

  override def simpleString: String = "Object"
}
