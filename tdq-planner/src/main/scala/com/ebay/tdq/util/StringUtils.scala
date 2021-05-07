package com.ebay.tdq.util

import java.util.regex.Pattern

/**
 * @author juntzhang
 */
object StringUtils {
  private[this] val trueStrings = Set("t", "true", "y", "yes", "1")
  private[this] val falseStrings = Set("f", "false", "n", "no", "0")

  /**
   * Validate and convert SQL 'like' pattern to a Java regular expression.
   *
   * Underscores (_) are converted to '.' and percent signs (%) are converted to '.*', other
   * characters are quoted literally. Escaping is done according to the rules specified in
   * [[Like]] usage documentation. An invalid pattern will
   * throw an [[Exception]].
   *
   * @param pattern    the SQL pattern to convert
   * @param escapeChar the escape string contains one character.
   * @return the equivalent Java regular expression of the pattern
   */
  def escapeLikeRegex(pattern: String, escapeChar: Char): String = {
    val in = pattern.toIterator
    val out = new StringBuilder()

    def fail(message: String) = throw new Exception(s"the pattern '$pattern' is invalid, $message")

    while (in.hasNext) {
      in.next match {
        case c1 if c1 == escapeChar && in.hasNext =>
          val c = in.next
          c match {
            case '_' | '%' => out ++= Pattern.quote(Character.toString(c))
            case c if c == escapeChar => out ++= Pattern.quote(Character.toString(c))
            case _ => fail(s"the escape character is not allowed to precede '$c'")
          }
        case c if c == escapeChar => fail("it is not allowed to end with the escape character")
        case '_' => out ++= "."
        case '%' => out ++= ".*"
        case c => out ++= Pattern.quote(Character.toString(c))
      }
    }
    "(?s)" + out.result() // (?s) enables dotall mode, causing "." to match new lines
  }

  def isTrueString(s: String): Boolean = trueStrings.contains(s.toLowerCase)

  def isFalseString(s: String): Boolean = falseStrings.contains(s.toLowerCase)

}
