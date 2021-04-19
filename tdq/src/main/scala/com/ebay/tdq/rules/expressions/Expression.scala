package com.ebay.tdq.rules.expressions

import java.util

/**
 * @author juntzhang
 */
// TODO: need add to flink metric
object TimeCost {
  val debug = new util.HashMap[Expression, Long]()
}

trait Expression {

  def dataType: DataType

  def children: Seq[Expression]

  def nullable: Boolean

  def cacheKey: Option[String]

  // TODO gen code instead of recursion, recursion performance is not very good!
  //  protected def doGenCode(ctx: CodegenContext, ev: ExprCode): ExprCode

  final def call(input: InternalRow, fromCache: Boolean): Any = {
    val s1 = System.currentTimeMillis()
    try {
      var t: Any = null
      val cacheInd = fromCache && cacheKey.isDefined && cacheKey.get.nonEmpty
      if (cacheInd) {
        t = input.cachedData.get(cacheKey.get)
      }
      if (cacheInd || t != null) {
        return t
      }
      val v = eval(input, fromCache)
      input.cache(cacheKey, v)
      v
    } finally {
      val t = System.currentTimeMillis() - s1
      TimeCost.debug.put(this, TimeCost.debug.getOrDefault(this, 0L) + t)
    }
  }

  def nodeName: String = getClass.getSimpleName

  def prettyName: String = ""

  def sql: String = {
    val childrenSQL = children.map(_.sql).mkString(", ")
    s"$prettyName($childrenSQL)"
  }

  protected def eval(input: InternalRow, fromCache: Boolean): Any

  def checkInputDataTypes(): TypeCheckResult = TypeCheckResult.TypeCheckSuccess

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

  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    val value = child.call(input, fromCache)
    if (value == null) {
      null
    } else {
      val s = System.currentTimeMillis()
      try {
        nullSafeEval(value)
      } finally {
        val t = System.currentTimeMillis() - s
        //        TimeCost.debug.put(this, TimeCost.debug.getOrDefault(this, 0L) + t)
      }
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

  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    val value1 = left.call(input, fromCache)
    if (value1 == null) {
      null
    } else {
      val value2 = right.call(input, fromCache)
      if (value2 == null) {
        null
      } else {
        val s = System.currentTimeMillis()
        try {
          nullSafeEval(value1, value2)
        } finally {
          val t = System.currentTimeMillis() - s
          //          TimeCost.debug.put(this, TimeCost.debug.getOrDefault(this, 0L) + t)
        }
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
}

abstract class TernaryExpression extends Expression {
  override def nullable: Boolean = children.exists(_.nullable)

  /**
   * Default behavior of evaluation according to the default nullability of TernaryExpression.
   * If subclass of TernaryExpression override nullable, probably should also override this.
   */
  protected override def eval(input: InternalRow, fromCache: Boolean): Any = {
    val exprs = children
    val value1 = exprs.head.call(input, fromCache)
    if (value1 != null) {
      val value2 = exprs(1).call(input, fromCache)
      if (value2 != null) {
        val value3 = exprs(2).call(input, fromCache)
        if (value3 != null) {
          val s = System.currentTimeMillis()
          try {
            return nullSafeEval(value1, value2, value3)
          } finally {
            val t = System.currentTimeMillis() - s
            //            TimeCost.debug.put(this, TimeCost.debug.getOrDefault(this, 0L) + t)
          }
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
