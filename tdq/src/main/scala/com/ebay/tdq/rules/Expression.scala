package com.ebay.tdq.rules

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.common.util.SOJParseClientInfo
import org.apache.calcite.sql.fun.SqlStdOperatorTable
import org.apache.calcite.sql.{SqlBinaryOperator, SqlOperator}
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
trait Expression {
  //  def metricName: String
  //
  //  def exprName: String
}

abstract sealed class Aggregation extends Expression {
  override def toString = s"Aggregate"

  def children: Seq[Expression]
}

abstract class BinaryExpression extends Expression {
  def left: Expression

  def right: Expression

  def children = Seq(left, right)
}

abstract class BinaryArithmetic extends BinaryExpression {
  def sqlOperator: SqlOperator
}

case class Plus(left: Expression, right: Expression) extends BinaryArithmetic {
  override def toString = s"($left + $right)"

  val sqlOperator: SqlBinaryOperator = SqlStdOperatorTable.PLUS
}

case class Sum(child: Expression) extends Aggregation {
  override def children: Seq[Expression] = Seq(child)

  override def toString = s"sum($child)"
}

case class TagExtract(tags: Seq[String]) {
  def run(e: RawEvent): String = {
    for (tag <- tags) {
      var v = e.getSojA.get(tag)
      if (StringUtils.isNotBlank(v)) {
        return v
      }
      v = e.getSojC.get(tag)
      if (StringUtils.isNotBlank(v)) {
        return v
      }
      v = e.getSojK.get(tag)
      if (StringUtils.isNotBlank(v)) {
        return v
      }
      return SOJParseClientInfo.getClientInfo(e.getClientData.toString, tag)
    }
    null
  }
}


case class Not() {
  def run(v: Boolean): Boolean = {
    !v
  }
}

case class IsNull() {
  def run(v: Any): Boolean = {
    v == null
  }
}

case class Count() {
  def run(v: Long): Long = {
    v + 1
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    var cnt = 0

    if (IsNull().run(TagExtract("itm|itmid|itm_id|itmlist|litm".split("\\|")).run(new RawEvent()))) {
      Count().run(cnt)
    }

  }
}