package com.ebay.tdq.expressions

import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
trait Predicate extends Expression {
  override def dataType: DataType = BooleanType

  override def cacheKey: Option[String] = None
}

case class Not(child: Expression)
  extends UnaryExpression with Predicate with ImplicitCastInputTypes with NullIntolerant {

  override def toString: String = s"NOT $child"

  override def inputTypes: Seq[DataType] = Seq(BooleanType)

  override def sql: String = s"(NOT ${child.sql})"

  // +---------+-----------+
  // | CHILD   | NOT CHILD |
  // +---------+-----------+
  // | TRUE    | FALSE     |
  // | FALSE   | TRUE      |
  // | UNKNOWN | UNKNOWN   |
  // +---------+-----------+
  protected override def nullSafeEval(input: Any): Any = !input.asInstanceOf[Boolean]
}

case class In(value: Expression, list: Seq[Expression]) extends Predicate {
  require(list != null, "list should not be null")
  private lazy val ordering = TypeUtils.getInterpretedOrdering(value.dataType)

  override def checkInputDataTypes(): TypeCheckResult = {
    val mismatchOpt = list.find(l => l.dataType != value.dataType)
    if (mismatchOpt.isDefined) {
      TypeCheckResult.TypeCheckFailure(s"Arguments must be same type but were: " +
        s"${value.dataType.catalogString} != ${mismatchOpt.get.dataType.catalogString}")
    } else {
      TypeCheckResult.TypeCheckSuccess
    }
  }

  override def nullable: Boolean = children.exists(_.nullable)

  override def children: Seq[Expression] = value +: list

  override def toString: String = s"$value IN ${list.mkString("(", ",", ")")}"

  override def sql: String = {
    val valueSQL = value.sql
    val listSQL = list.map(_.sql).mkString(", ")
    s"($valueSQL IN ($listSQL))"
  }

  protected override def eval(input: InternalRow): Any = {
    val evaluatedValue = value.call(input)
    if (evaluatedValue == null) {
      null
    } else {
      var hasNull = false
      list.foreach { e =>
        val v = e.call(input)
        if (v == null) {
          hasNull = true
        } else if (ordering.equiv(v, evaluatedValue)) {
          return true
        }
      }
      if (hasNull) {
        null
      } else {
        false
      }
    }
  }


}

case class And(left: Expression, right: Expression) extends BinaryOperator with Predicate {
  override def inputType: DataType = BooleanType

  override def symbol: String = "&&"

  override def sqlOperator: String = "AND"

  // +---------+---------+---------+---------+
  // | AND     | TRUE    | FALSE   | UNKNOWN |
  // +---------+---------+---------+---------+
  // | TRUE    | TRUE    | FALSE   | UNKNOWN |
  // | FALSE   | FALSE   | FALSE   | FALSE   |
  // | UNKNOWN | UNKNOWN | FALSE   | UNKNOWN |
  // +---------+---------+---------+---------+
  protected override def eval(input: InternalRow): Any = {
    val input1 = left.call(input)
    if (input1 == false) {
      false
    } else {
      val input2 = right.call(input)
      if (input2 == false) {
        false
      } else {
        if (input1 != null && input2 != null) {
          true
        } else {
          null
        }
      }
    }
  }
}

case class Or(left: Expression, right: Expression) extends BinaryOperator with Predicate {
  override def inputType: DataType = BooleanType

  override def symbol: String = "||"

  override def sqlOperator: String = "OR"

  // +---------+---------+---------+---------+
  // | OR      | TRUE    | FALSE   | UNKNOWN |
  // +---------+---------+---------+---------+
  // | TRUE    | TRUE    | TRUE    | TRUE    |
  // | FALSE   | TRUE    | FALSE   | UNKNOWN |
  // | UNKNOWN | TRUE    | UNKNOWN | UNKNOWN |
  // +---------+---------+---------+---------+
  protected override def eval(input: InternalRow): Any = {
    val input1 = left.call(input)
    if (input1 == true) {
      true
    } else {
      val input2 = right.call(input)
      if (input2 == true) {
        true
      } else {
        if (input1 != null && input2 != null) {
          false
        } else {
          null
        }
      }
    }
  }
}

abstract class BinaryComparison extends BinaryOperator with Predicate {

  protected lazy val ordering: Ordering[Any] = TypeUtils.getInterpretedOrdering(left.dataType)

  // Note that we need to give a superset of allowable input types since orderable types are not
  // finitely enumerable. The allowable types are checked below by checkInputDataTypes.
  override def inputType: AbstractDataType = AnyDataType

  override def checkInputDataTypes(): TypeCheckResult = super.checkInputDataTypes() match {
    case TypeCheckResult.TypeCheckSuccess =>
      TypeUtils.checkForOrderingExpr(left.dataType, this.getClass.getSimpleName)
    case failure => failure
  }
}

case class EqualTo(left: Expression, right: Expression)
  extends BinaryComparison with NullIntolerant {

  override def symbol: String = "="

  // +---------+---------+---------+---------+
  // | =       | TRUE    | FALSE   | UNKNOWN |
  // +---------+---------+---------+---------+
  // | TRUE    | TRUE    | FALSE   | UNKNOWN |
  // | FALSE   | FALSE   | TRUE    | UNKNOWN |
  // | UNKNOWN | UNKNOWN | UNKNOWN | UNKNOWN |
  // +---------+---------+---------+---------+
  protected override def nullSafeEval(left: Any, right: Any): Any = ordering.equiv(left, right)
}

case class LessThan(left: Expression, right: Expression)
  extends BinaryComparison with NullIntolerant {

  override def symbol: String = "<"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = ordering.lt(input1, input2)
}

case class LessThanOrEqual(left: Expression, right: Expression)
  extends BinaryComparison with NullIntolerant {

  override def symbol: String = "<="

  protected override def nullSafeEval(input1: Any, input2: Any): Any = ordering.lteq(input1, input2)
}

case class GreaterThan(left: Expression, right: Expression)
  extends BinaryComparison with NullIntolerant {

  override def symbol: String = ">"

  protected override def nullSafeEval(input1: Any, input2: Any): Any = ordering.gt(input1, input2)
}

case class GreaterThanOrEqual(left: Expression, right: Expression)
  extends BinaryComparison with NullIntolerant {

  override def symbol: String = ">="

  protected override def nullSafeEval(input1: Any, input2: Any): Any = ordering.gteq(input1, input2)
}
