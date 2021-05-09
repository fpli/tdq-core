package com.ebay.tdq.expressions

import java.util.Locale
import java.util.regex.{MatchResult, Pattern}

import com.ebay.tdq.types._
import com.ebay.tdq.utils.StringUtils

/**
 * @author juntzhang
 */
abstract class StringRegexExpression extends BinaryExpression
  with ImplicitCastInputTypes with NullIntolerant {

  // try cache the pattern for Literal
  private lazy val cache: Pattern = right match {
    case x@Literal(value: String, StringType) => compile(value)
    case _ => null
  }

  def escape(v: String): String

  def matches(regex: Pattern, str: String): Boolean

  override def dataType: DataType = BooleanType

  override def inputTypes: Seq[DataType] = Seq(StringType, StringType)

  override def sql: String = s"${left.sql} ${prettyName.toUpperCase(Locale.ROOT)} ${right.sql}"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = {
    val regex = pattern(input2.asInstanceOf[String])
    if (regex == null) {
      null
    } else {
      matches(regex, input1.asInstanceOf[String])
    }
  }

  protected def pattern(str: String): Pattern = if (cache == null) compile(str) else cache

  protected def compile(str: String): Pattern = if (str == null) {
    null
  } else {
    // Let it raise exception if couldn't compile the regex string
    Pattern.compile(escape(str))
  }
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
    if (!p.equals(lastRegex)) {
      // regex value changed
      lastRegex = p.asInstanceOf[String]
      pattern = Pattern.compile(lastRegex)
    }
    val m = pattern.matcher(s.toString)
    if (m.find) {
      val mr: MatchResult = m.toMatchResult
      mr.group(r.asInstanceOf[Number].intValue())
    } else {
      null
    }
  }

  override def dataType: DataType = StringType

  override def inputTypes: Seq[AbstractDataType] = Seq(StringType, StringType, IntegerType)

  override def children: Seq[Expression] = subject :: regexp :: idx :: Nil

  override def prettyName: String = "regexp_extract"
}
