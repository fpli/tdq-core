package com.ebay.tdq.expressions

import com.ebay.tdq.types.{AbstractDataType, DataType}

/**
 * @author juntzhang
 */
trait Expression extends Serializable {
  def dataType: DataType

  def children: Seq[Expression]

  def nullable: Boolean

  def foldable: Boolean = false

  lazy val simpleName = this.getClass.getSimpleName.split("\\$")(0)

  def cacheKey: Option[String]

  final def call(input: InternalRow = null): Any = {
    if (input == null) {
      return eval(input)
    }
    if (input.containsKey(cacheKey)) {
      return input.getCache(cacheKey.get)
    }
    val v = eval(input)
    input.putCache(cacheKey, v)
    v
  }

  def nodeName: String = getClass.getSimpleName

  def prettyName: String = ""

  def sql: String = {
    val childrenSQL = children.map(_.sql).mkString(", ")
    s"$prettyName($childrenSQL)"
  }

  def checkInputDataTypes(): TypeCheckResult = TypeCheckResult.TypeCheckSuccess

  protected def eval(input: InternalRow): Any

}

/**
 * When an expression inherits this, meaning the expression is null intolerant (i.e. any null
 * input will result in null output). We will use this information during constructing IsNotNull
 * constraints.
 */
trait NullIntolerant extends Expression

abstract class LeafExpression extends Expression {
  override final def children: Seq[Expression] = Nil
}

abstract class UnaryExpression extends Expression {
  override final def children: Seq[Expression] = child :: Nil

  def child: Expression

  override def nullable: Boolean = child.nullable

  protected override def eval(input: InternalRow): Any = {
    val value = child.call(input)
    if (value == null) {
      null
    } else {
      nullSafeEval(value)
    }
  }

  protected def nullSafeEval(input: Any): Any =
    sys.error(s"UnaryExpressions must override either eval or nullSafeEval")
}

abstract class BinaryExpression extends Expression {
  override final def children: Seq[Expression] = Seq(left, right)

  def left: Expression

  def right: Expression

  override def nullable: Boolean = left.nullable || right.nullable

  protected override def eval(input: InternalRow): Any = {
    val value1 = left.call(input)
    if (value1 == null) {
      null
    } else {
      val value2 = right.call(input)
      if (value2 == null) {
        null
      } else {
        nullSafeEval(value1, value2)
      }
    }
  }

  protected def nullSafeEval(input1: Any, input2: Any): Any =
    sys.error(s"BinaryExpressions must override either eval or nullSafeEval")

}

abstract class BinaryOperator extends BinaryExpression with ExpectsInputTypes {
  def symbol: String

  def inputType: AbstractDataType

  override def inputTypes: Seq[AbstractDataType] = Seq(inputType, inputType)

  override def toString: String = s"($left $symbol $right)"

  override def sql: String = s"(${left.sql} $sqlOperator ${right.sql})"

  def sqlOperator: String = symbol

  def copy: this.type ={
    this
  }
}

object BinaryOperator {
  def unapply(e: BinaryOperator): Option[(Expression, Expression)] = Some((e.left, e.right))
}

abstract class TernaryExpression extends Expression {
  override def nullable: Boolean = children.exists(_.nullable)

  /**
   * Default behavior of evaluation according to the default nullability of TernaryExpression.
   * If subclass of TernaryExpression override nullable, probably should also override this.
   */
  protected override def eval(input: InternalRow): Any = {
    val exprs = children
    val value1 = exprs.head.call(input)
    if (value1 != null) {
      val value2 = exprs(1).call(input)
      if (value2 != null) {
        val value3 = exprs(2).call(input)
        if (value3 != null) {
          return nullSafeEval(value1, value2, value3)
        }
      }
    }
    null
  }

  /**
   * Called by default [[eval]] implementation.  If subclass of TernaryExpression keep the default
   * nullability, they can override this method to save null-check code.  If we need full control
   * of evaluation process, we should override [[eval]].
   */
  protected def nullSafeEval(input1: Any, input2: Any, input3: Any): Any =
    sys.error(s"TernaryExpressions must override either eval or nullSafeEval")
}