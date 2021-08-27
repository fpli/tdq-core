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