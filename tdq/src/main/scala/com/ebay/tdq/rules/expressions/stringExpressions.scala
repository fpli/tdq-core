package com.ebay.tdq.rules.expressions

import java.lang.{String => UTF8String}

/**
 * @author juntzhang
 */
abstract class StringPredicate extends BinaryExpression
  with Predicate with ImplicitCastInputTypes with NullIntolerant {

  def compare(l: UTF8String, r: UTF8String): Boolean

  override def inputTypes: Seq[DataType] = Seq(StringType, StringType)

  override def toString: String = s"$nodeName($left, $right)"

  protected override def nullSafeEval(input1: Any, input2: Any): Any =
    compare(input1.asInstanceOf[UTF8String], input2.asInstanceOf[UTF8String])
}

case class Contains(left: Expression, right: Expression, override val cacheKey: Option[String] = None) extends StringPredicate {
  override def compare(l: UTF8String, r: UTF8String): Boolean = l.contains(r)
}

/**
 * A function that returns true if the string `left` starts with the string `right`.
 */
case class StartsWith(left: Expression, right: Expression, override val cacheKey: Option[String] = None) extends StringPredicate {
  override def compare(l: UTF8String, r: UTF8String): Boolean = l.startsWith(r)
}

/**
 * A function that returns true if the string `left` ends with the string `right`.
 */
case class EndsWith(left: Expression, right: Expression, override val cacheKey: Option[String] = None) extends StringPredicate {
  override def compare(l: UTF8String, r: UTF8String): Boolean = l.endsWith(r)
}

case class StringReplace(
  srcExpr: Expression,
  searchExpr: Expression,
  replaceExpr: Expression,
  cacheKey: Option[String] = None
)
  extends TernaryExpression with ImplicitCastInputTypes {

  def this(srcExpr: Expression, searchExpr: Expression, cacheKey: Option[String]) = {
    this(srcExpr, searchExpr, Literal(""), cacheKey)
  }

  override def nullSafeEval(srcEval: Any, searchEval: Any, replaceEval: Any): Any = {
    srcEval.asInstanceOf[UTF8String].replace(
      searchEval.asInstanceOf[UTF8String], replaceEval.asInstanceOf[UTF8String])
  }

  override def dataType: DataType = StringType

  override def inputTypes: Seq[DataType] = Seq(StringType, StringType, StringType)

  override def children: Seq[Expression] = srcExpr :: searchExpr :: replaceExpr :: Nil

  override def prettyName: String = "replace"
}

case class Length(child: Expression, cacheKey: Option[String] = None) extends UnaryExpression with ImplicitCastInputTypes {
  override def dataType: DataType = IntegerType

  override def inputTypes: Seq[AbstractDataType] = Seq(TypeCollection(StringType, BinaryType))

  protected override def nullSafeEval(value: Any): Any = child.dataType match {
    case StringType => value.asInstanceOf[String].length
    case BinaryType => value.asInstanceOf[Array[Byte]].length
  }
}