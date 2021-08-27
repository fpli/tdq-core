package com.ebay.tdq.expressions

import com.ebay.tdq.types._

/**
 * @author juntzhang
 */
case class If(predicate: Expression, trueValue: Expression, falseValue: Expression, cacheKey: Option[String] = None)
  extends Expression {

  override def children: Seq[Expression] = predicate :: trueValue :: falseValue :: Nil

  override def nullable: Boolean = trueValue.nullable || falseValue.nullable

  override def checkInputDataTypes(): TypeCheckResult = {
    if (predicate.dataType != BooleanType) {
      TypeCheckResult.TypeCheckFailure(
        "type of predicate expression in If should be boolean, " +
          s"not ${predicate.dataType.simpleString}")
    } else if (!trueValue.dataType.sameType(falseValue.dataType)) {
      TypeCheckResult.TypeCheckFailure(s"differing types in '$sql' " +
        s"(${trueValue.dataType.simpleString} and ${falseValue.dataType.simpleString}).")
    } else {
      TypeCheckResult.TypeCheckSuccess
    }
  }

  override def sql: String = s"(IF(${predicate.sql}, ${trueValue.sql}, ${falseValue.sql}))"

  override def dataType: DataType = trueValue.dataType

  override def toString: String = s"if ($predicate) $trueValue else $falseValue"

  protected override def eval(input: InternalRow): Any = {
    if (java.lang.Boolean.TRUE.equals(predicate.call(input))) {
      trueValue.call(input)
    } else {
      falseValue.call(input)
    }
  }
}

case class CaseWhen(
  branches: Seq[(Expression, Expression)],
  elseValue: Option[Expression] = None,
  cacheKey: Option[String] = None)
  extends Expression {

  def this(branches: Seq[(Expression, Expression)], elseValue: Expression) =
    this(branches, Some(elseValue))

  override def children: Seq[Expression] = branches.flatMap(b => b._1 :: b._2 :: Nil) ++ elseValue

  override def dataType: DataType = branches.head._2.dataType

  override def nullable: Boolean = {
    // Result is nullable if any of the branch is nullable, or if the else value is nullable
    branches.exists(_._2.nullable) || elseValue.map(_.nullable).getOrElse(true)
  }

  override def checkInputDataTypes(): TypeCheckResult = {
    // Make sure all branch conditions are boolean types.
    if (valueTypesEqual) {
      if (branches.forall(_._1.dataType == BooleanType)) {
        TypeCheckResult.TypeCheckSuccess
      } else {
        val index = branches.indexWhere(_._1.dataType != BooleanType)
        TypeCheckResult.TypeCheckFailure(
          s"WHEN expressions in CaseWhen should all be boolean type, " +
            s"but the ${index + 1}th when expression's type is ${branches(index)._1}")
      }
    } else {
      TypeCheckResult.TypeCheckFailure(
        "THEN and ELSE expressions should all be same type or coercible to a common type")
    }
  }

  def valueTypesEqual: Boolean = valueTypes.size <= 1 || valueTypes.sliding(2, 1).forall {
    case Seq(dt1, dt2) => dt1.sameType(dt2)
  }

  // both then and else expressions should be considered.
  def valueTypes: Seq[DataType] = branches.map(_._2.dataType) ++ elseValue.map(_.dataType)

  override def toString: String = {
    val cases = branches.map { case (c, v) => s" WHEN $c THEN $v" }.mkString
    val elseCase = elseValue.map(" ELSE " + _).getOrElse("")
    "CASE" + cases + elseCase + " END"
  }

  override def sql: String = {
    val cases = branches.map { case (c, v) => s" WHEN ${c.sql} THEN ${v.sql}" }.mkString
    val elseCase = elseValue.map(" ELSE " + _.sql).getOrElse("")
    "CASE" + cases + elseCase + " END"
  }

  protected override def eval(input: InternalRow): Any = {
    var i = 0
    val size = branches.size
    while (i < size) {
      if (java.lang.Boolean.TRUE.equals(branches(i)._1.call(input))) {
        return branches(i)._2.call(input)
      }
      i += 1
    }
    if (elseValue.isDefined) {
      elseValue.get.call(input)
    } else {
      null
    }
  }
}

object CaseWhen {
  def apply(branches: Seq[(Expression, Expression)], elseValue: Expression): CaseWhen = {
    CaseWhen(branches, Option(elseValue))
  }

  /**
   * A factory method to facilitate the creation of this expression when used in parsers.
   *
   * @param branches Expressions at even position are the branch conditions, and expressions at odd
   *                 position are branch values.
   */
  def createFromParser(branches: Seq[Expression]): CaseWhen = {
    val cases = branches.grouped(2).flatMap {
      case cond :: value :: Nil => Some((cond, value))
      case value :: Nil => None
    }.toArray.toSeq // force materialization to make the seq serializable
    val elseValue = if (branches.size % 2 == 1) Some(branches.last) else None
    CaseWhen(cases, elseValue)
  }
}

object CaseKeyWhen {
  def apply(key: Expression, branches: Seq[Expression]): CaseWhen = {
    val cases = branches.grouped(2).flatMap {
      case Seq(cond, value) => Some((EqualTo(key, cond), value))
      case Seq(value) => None
    }.toArray.toSeq // force materialization to make the seq serializable
    val elseValue = if (branches.size % 2 == 1) Some(branches.last) else None
    CaseWhen(cases, elseValue)
  }
}
