package com.ebay.tdq.expressions

import java.lang.{String => UTF8String}

import com.ebay.tdq.planner.utils.udf.common.ByteArray
import com.ebay.tdq.types._

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

trait String2TrimExpression extends Expression with ImplicitCastInputTypes {

  override def dataType: DataType = StringType

  override def inputTypes: Seq[AbstractDataType] = Seq.fill(children.size)(StringType)

  override def nullable: Boolean = children.exists(_.nullable)
}

case class StringTrim(
  srcStr: Expression,
  trimStr: Option[Expression] = None,
  cacheKey: Option[String] = None)
  extends String2TrimExpression {

  def this(trimStr: Expression, srcStr: Expression) = this(srcStr, Option(trimStr))

  def this(srcStr: Expression) = this(srcStr, None)

  override def prettyName: String = "trim"

  override def children: Seq[Expression] = if (trimStr.isDefined) {
    srcStr :: trimStr.get :: Nil
  } else {
    srcStr :: Nil
  }

  protected override def eval(input: InternalRow): Any = {
    val srcString = srcStr.call(input).asInstanceOf[UTF8String]
    if (srcString == null) {
      null
    } else {
      if (trimStr.isDefined) {
        new RuntimeException("not supported!")
        //srcString.trim(trimStr.get.call(input, fromCache).asInstanceOf[UTF8String])
      } else {
        srcString.trim()
      }
    }
  }
}

trait String2StringExpression extends ImplicitCastInputTypes {
  self: UnaryExpression =>

  def convert(v: String): String

  override def dataType: DataType = StringType

  override def inputTypes: Seq[DataType] = Seq(StringType)

  protected override def nullSafeEval(input: Any): Any =
    convert(input.asInstanceOf[UTF8String])
}

case class Lower(child: Expression, cacheKey: Option[String] = None) extends UnaryExpression with String2StringExpression {

  override def convert(v: String): String = v.toLowerCase
}

case class Upper(child: Expression, cacheKey: Option[String] = None)
  extends UnaryExpression with String2StringExpression {

  override def convert(v: UTF8String): UTF8String = v.toUpperCase

}

case class Substring(str: Expression, pos: Expression, len: Expression, cacheKey: Option[String] = None)
  extends TernaryExpression with ImplicitCastInputTypes with NullIntolerant {

  def this(str: Expression, pos: Expression, cacheKey: Option[String]) = {
    this(str, pos, Literal(Integer.MAX_VALUE), cacheKey)
  }

  override def dataType: DataType = str.dataType

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(StringType, BinaryType), IntegerType, IntegerType)

  override def children: Seq[Expression] = str :: pos :: len :: Nil

  def substringSQL(string: String, pos: Int, length: Int): String = { // Information regarding the pos calculation:
    val len = string.length
    val start = if (pos > 0) pos - 1
    else if ((pos < 0)) len + pos
    else 0
    val `end` = if (length == Integer.MAX_VALUE) len
    else start + length
    string.substring(start, `end`)
  }

  override def nullSafeEval(string: Any, pos: Any, len: Any): Any = {
    str.dataType match {
      case StringType =>
        substringSQL(string.asInstanceOf[UTF8String], pos.asInstanceOf[Int], len.asInstanceOf[Int])
      case BinaryType =>
        ByteArray.subStringSQL(string.asInstanceOf[Array[Byte]], pos.asInstanceOf[Int], len.asInstanceOf[Int])
    }
  }
}

case class StringSplit(str: Expression, pattern: Expression, cacheKey: Option[String] = None)
  extends BinaryExpression with ImplicitCastInputTypes {

  override def left: Expression = str

  override def right: Expression = pattern

  override def dataType: DataType = ArrayType(StringType)

  override def inputTypes: Seq[DataType] = Seq(StringType, StringType)

  override def nullSafeEval(string: Any, regex: Any): Any = {
    val strings = string.asInstanceOf[UTF8String].split(regex.asInstanceOf[UTF8String], -1)
    strings.asInstanceOf[Array[Any]]
  }


  override def prettyName: String = "split"
}