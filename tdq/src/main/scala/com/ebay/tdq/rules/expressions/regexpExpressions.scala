package com.ebay.tdq.rules.expressions

import java.util.Locale
import java.util.regex.Pattern

import com.ebay.tdq.rules.physical.TdqUDFs
import com.ebay.tdq.rules.util.StringUtils

/**
 * @author juntzhang
 */
abstract class StringRegexExpression extends BinaryExpression
  with ImplicitCastInputTypes with NullIntolerant {

  def escape(v: String): String

  def matches(regex: Pattern, str: String): Boolean

  override def dataType: DataType = BooleanType

  override def inputTypes: Seq[DataType] = Seq(StringType, StringType)

  // try cache the pattern for Literal
  private lazy val cache: Pattern = right match {
    case x@Literal(value: String, StringType) => compile(value)
    case _ => null
  }

  protected def compile(str: String): Pattern = if (str == null) {
    null
  } else {
    // Let it raise exception if couldn't compile the regex string
    Pattern.compile(escape(str))
  }

  protected def pattern(str: String): Pattern = if (cache == null) compile(str) else cache

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    val regex = pattern(input2.asInstanceOf[String])
    if (regex == null) {
      null
    } else {
      matches(regex, input1.asInstanceOf[String])
    }
  }

  override def sql: String = s"${left.sql} ${prettyName.toUpperCase(Locale.ROOT)} ${right.sql}"
}

case class Like(left: Expression, right: Expression, escapeChar: Char = '\\', cacheKey: Option[String] = None)
  extends StringRegexExpression {

  override def escape(v: String): String = StringUtils.escapeLikeRegex(v, escapeChar)

  override def matches(regex: Pattern, str: String): Boolean = regex.matcher(str).matches()

  override def toString: String = escapeChar match {
    case '\\' => s"$left LIKE $right"
    case c => s"$left LIKE $right ESCAPE '$c'"
  }
}

case class RLike(left: Expression, right: Expression, cacheKey: Option[String] = None) extends StringRegexExpression {
  override def escape(v: String): String = v

  override def matches(regex: Pattern, str: String): Boolean = regex.matcher(str).find(0)

  override def toString: String = s"$left RLIKE $right"
}


case class RegExpExtract(subject: Expression, regexp: Expression, idx: Expression = Literal(1), cacheKey: Option[String] = None)
  extends TernaryExpression with ImplicitCastInputTypes {

  // last regex in string, we will update the pattern iff regexp value changed.
  @transient private var lastRegex: String = _
  // last regex pattern, we cache it for performance concern
  @transient private var pattern: Pattern = _

  override def nullSafeEval(s: Any, p: Any, r: Any): Any = {
    TdqUDFs.regexpExtract(s.asInstanceOf[String], p.asInstanceOf[String], r.asInstanceOf[Int])
  }

  override def dataType: DataType = StringType

  override def inputTypes: Seq[AbstractDataType] = Seq(StringType, StringType, IntegerType)

  override def children: Seq[Expression] = subject :: regexp :: idx :: Nil

  override def prettyName: String = "regexp_extract"
}
