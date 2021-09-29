package com.ebay.tdq.expressions.analysis


import com.ebay.tdq.expressions._
import com.ebay.tdq.rules.RegistryContext
import com.ebay.tdq.types._
import javax.annotation.Nullable
import org.apache.log4j.Logger

import scala.util.Try

/**
 * @see ImplicitTypeCasts
 * @author juntzhang
 */
object TypeCoercionRule {
  val log: Logger = Logger.getLogger("TypeCoercionRule")

  def copy(opt: BinaryExpression, name: String, value: Expression): BinaryExpression = {
    val clazz = opt.getClass
    Try(opt.getClass.getDeclaredField(name)).toOption match {
      case Some(field) =>
        field.setAccessible(true)
        clazz.getMethod(name).invoke(opt)
        field.set(opt, value)
      case _ =>
    }
    opt
  }

  def copy2(opt: Expression, name: String, value: Any): Expression = {
    val clazz = opt.getClass
    Try(opt.getClass.getDeclaredField(name)).toOption match {
      case Some(field) =>
        field.setAccessible(true)
        clazz.getMethod(name).invoke(opt)
        field.set(opt, value)
      case _ =>
    }
    opt
  }

  val numericPrecedence =
    IndexedSeq(
      ByteType,
      ShortType,
      IntegerType,
      LongType,
      FloatType,
      DoubleType)

  val findTightestCommonType: (DataType, DataType) => Option[DataType] = {
    case (t1, t2) if t1 == t2 => Some(t1)
    case (NullType, t1) => Some(t1)
    case (t1, NullType) => Some(t1)

    case (t1: IntegralType, t2: DecimalType) if t2.isWiderThan(t1) =>
      Some(t2)
    case (t1: DecimalType, t2: IntegralType) if t1.isWiderThan(t2) =>
      Some(t1)

    // Promote numeric types to the highest of the two
    case (t1: NumericType, t2: NumericType)
      if !t1.isInstanceOf[DecimalType] && !t2.isInstanceOf[DecimalType] =>
      val index = numericPrecedence.lastIndexWhere(t => t == t1 || t == t2)
      Some(numericPrecedence(index))

    case (_: TimestampType, _: DateType) | (_: DateType, _: TimestampType) =>
      Some(TimestampType)
    case (StringType, t2: AtomicType) if t2 != BinaryType && t2 != BooleanType => Some(StringType)
    case (t1: AtomicType, StringType) if t1 != BinaryType && t1 != BooleanType => Some(StringType)
    case _ => None
  }

  def coerceTypes(expr: Expression): Expression = {
    expr match {
      case b@BinaryOperator(left, right) =>
        (left, right) match {
          case (l: Literal, r) if r.dataType.isInstanceOf[DecimalType] && l.dataType.isInstanceOf[IntegralType] =>
            copy(b, "left", Cast(l, DecimalType.fromLiteral(l), forPrecision = true))
          case (l, r: Literal) if l.dataType.isInstanceOf[DecimalType] && r.dataType.isInstanceOf[IntegralType] =>
            copy(b, "right", Cast(r, DecimalType.fromLiteral(r), forPrecision = true))
          // Promote integers inside a binary expression with fixed-precision decimals to decimals,
          // and fixed-precision decimals in an expression with floats / doubles to doubles
          case (l@IntegralType(), r@DecimalType.Expression(_, _)) =>
            copy(b, "left", Cast(l, DecimalType.forType(l.dataType), forPrecision = true))
          case (DecimalType.Expression(_, _), r@IntegralType()) =>
            copy(b, "right", Cast(r, DecimalType.forType(r.dataType), forPrecision = true))
          case _ =>
            findTightestCommonType(left.dataType, right.dataType).map { commonType =>
              if (b.inputType.acceptsType(commonType)) {
                // If the expression accepts the tightest common type, cast to that.
                if (left.dataType != commonType) {
                  copy(b, "left", Cast(left, commonType))
                }
                if (right.dataType != commonType) {
                  copy(b, "right", Cast(right, commonType))
                }
              }
              b
            }.getOrElse(b) // If there is no applicable conversion, leave expression unchanged.
        }
      case e: ImplicitCastInputTypes if e.inputTypes.nonEmpty =>
        val children: Seq[Expression] = e.children.zip(e.inputTypes).map { case (in, expected) =>
          // If we cannot do the implicit cast, just use the original input.
          implicitCast(in, expected).getOrElse(in)
        }
        e match {
          case ue: UnaryExpression =>
            copy2(ue, "child", children.head)
          case _ =>
            if (children.zip(e.children).exists { case (e1, e2) => e1 != e2 }) {
              // todo need enhance to all type
              if (children.forall(c => c.isInstanceOf[Expression] || c.isInstanceOf[Option[String]])) {
                val args = (children :+ e.cacheKey).map(_.asInstanceOf[AnyRef]).toArray
                val varargCtor = e.getClass.getConstructors.find(_.getParameterCount == args.length)
                return RegistryContext.expression(varargCtor, args)
              } else {
                log.info(s"expr ${e.getClass.getName} constructor not support (${children.mkString(",")})")
              }
            }
            e
        }
      case o =>
        o
    }
  }

  def implicitCast(e: Expression, expectedType: AbstractDataType): Option[Expression] = {
    implicitCast(e.dataType, expectedType).map { dt =>
      if (dt == e.dataType) e else Cast(e, dt)
    }
  }

  private def implicitCast(inType: DataType, expectedType: AbstractDataType): Option[DataType] = {
    // Note that ret is nullable to avoid typing a lot of Some(...) in this local scope.
    // We wrap immediately an Option after this.
    @Nullable val ret: DataType = (inType, expectedType) match {
      // If the expected type is already a parent of the input type, no need to cast.
      case _ if expectedType.acceptsType(inType) => inType

      // Cast null type (usually from null literals) into target types
      case (NullType, target) => target.defaultConcreteType

      // If the function accepts any numeric type and the input is a string, we follow the hive
      // convention and cast that input into a double
      case (StringType, NumericType) => NumericType.defaultConcreteType

      // Implicit cast among numeric types. When we reach here, input type is not acceptable.

      // If input is a numeric type but not decimal, and we expect a decimal type,
      // cast the input to decimal.
      case (d: NumericType, DecimalType) => DecimalType.forType(d)
      // For any other numeric types, implicitly cast to each other, e.g. long -> int, int -> long
      case (_: NumericType, target: NumericType) => target

      // Implicit cast between date time types
      case (DateType, TimestampType) => TimestampType
      case (TimestampType, DateType) => DateType

      // Implicit cast from/to string
      case (StringType, DecimalType) => DecimalType.SYSTEM_DEFAULT
      case (StringType, target: NumericType) => target
      case (StringType, DateType) => DateType
      case (StringType, TimestampType) => TimestampType
      case (StringType, BinaryType) => BinaryType
      // Cast any atomic type to string.
      case (any, StringType) if any != StringType => StringType

      // When we reach here, input type is not acceptable for any types in this type collection,
      // try to find the first one we can implicitly cast.
      case (_, TypeCollection(types)) =>
        types.flatMap(implicitCast(inType, _)).headOption.orNull

      // Implicit cast between array types.
      //
      // Compare the nullabilities of the from type and the to type, check whether the cast of
      // the nullability is resolvable by the following rules:
      // 1. If the nullability of the to type is true, the cast is always allowed;
      // 2. If the nullability of the to type is false, and the nullability of the from type is
      // true, the cast is never allowed;
      // 3. If the nullabilities of both the from type and the to type are false, the cast is
      // allowed only when Cast.forceNullable(fromType, toType) is false.
      case (ArrayType(fromType, fn), ArrayType(toType: DataType, true)) =>
        implicitCast(fromType, toType).map(ArrayType(_, true)).orNull

      case (ArrayType(fromType, true), ArrayType(toType: DataType, false)) => null

      case (ArrayType(fromType, false), ArrayType(toType: DataType, false))
        if !Cast.forceNullable(fromType, toType) =>
        implicitCast(fromType, toType).map(ArrayType(_, false)).orNull

      case _ => null
    }
    Option(ret)
  }

}

