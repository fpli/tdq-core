package com.ebay.tdq.expressions

import com.ebay.tdq.planner.utils.udf.common.ByteArray
import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
case class Concat(children: Seq[Expression], cacheKey: Option[String] = None) extends Expression {

  val allowedTypes = Seq(StringType)

  override def checkInputDataTypes(): TypeCheckResult = {
    if (children.isEmpty) {
      TypeCheckResult.TypeCheckSuccess
    } else {
      val childTypes = children.map(_.dataType)
      if (childTypes.exists(tpe => !allowedTypes.exists(_.acceptsType(tpe)))) {
        return TypeCheckResult.TypeCheckFailure(
          s"input to function $prettyName should have been StringType" +
            s" but it's " + childTypes.map(_.simpleString).mkString("[", ", ", "]"))
      }
      TypeUtils.checkForSameTypeInputExpr(childTypes, s"function $prettyName")
    }
  }

  override def dataType: DataType = children.map(_.dataType).headOption.getOrElse(StringType)

  override def nullable: Boolean = children.exists(_.nullable)

  override def foldable: Boolean = children.forall(_.foldable)

  override def eval(input: InternalRow): Any = dataType match {
    case BinaryType =>
      val inputs = children.map(_.call(input).asInstanceOf[Array[Byte]])
      ByteArray.concat(inputs: _*)
    case StringType =>
      val inputs = children.map(_.call(input).asInstanceOf[String])
      inputs.mkString
    case _ =>
      throw new IllegalArgumentException
  }

  override def toString: String = s"concat(${children.mkString(", ")})"

  override def sql: String = s"concat(${children.map(_.sql).mkString(", ")})"
}

case class ElementAt(left: Expression, right: Expression, cacheKey: Option[String] = None) extends BinaryExpression
  with ImplicitCastInputTypes {

  def dataType: DataType = left.dataType match {
    case ArrayType(elementType, _) => elementType
    case MapType(_, valueType, _) => valueType
  }

  def inputTypes: Seq[AbstractDataType] = {
    Seq(TypeCollection(ArrayType, MapType),
      left.dataType match {
        case _: ArrayType => IntegerType
        case _: MapType => left.dataType.asInstanceOf[MapType].keyType
      }
    )
  }

  override def nullable: Boolean = true

  override def nullSafeEval(value: Any, ordinal: Any): Any = {
    left.dataType match {
      case _: ArrayType =>
        val array = value.asInstanceOf[Array[Any]]
        val index = ordinal.asInstanceOf[Int]
        if (array.length < math.abs(index)) {
          null
        } else {
          val idx = if (index == 0) {
            throw new ArrayIndexOutOfBoundsException("SQL array indices start at 1")
          } else if (index > 0) {
            index - 1
          } else {
            array.length + index
          }
          if (idx > array.length || idx < 0) {
            null
          } else {
            array(idx)
          }
        }
      case _: MapType =>
        value match {
          case m: Map[Any, Any] =>
            m.getOrElse(ordinal, null)
          case m: java.util.Map[Any, Any] =>
            m.getOrDefault(ordinal, null)
          case _ =>
            throw new IllegalStateException("not support " + value.getClass)
        }
    }
  }

  override def prettyName: String = "element_at"
}