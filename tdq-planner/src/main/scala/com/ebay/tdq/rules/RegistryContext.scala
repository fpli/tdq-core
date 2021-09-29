package com.ebay.tdq.rules

import java.lang.reflect.Constructor

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.expressions.Expression
import com.ebay.tdq.types.DataType
import com.ebay.tdq.utils.InstanceUtils

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
 * @author juntzhang
 */
case class RegistryContext(
  tdqEnv: TdqEnv,
  operatorName: String,
  operands: Array[Any],
  cacheKey: Option[String] = None,
  getDataType: Array[String] => DataType
) {
  def expression[T <: Expression](implicit tag: ClassTag[T]): Expression = {
    val args: Array[AnyRef] = (operands.toBuffer :+ cacheKey).map(_.asInstanceOf[AnyRef]).toArray
    val varargCtor: Option[Constructor[_]] = tag.runtimeClass.getConstructors.find(_.getParameterCount == args.length)
    RegistryContext.expression(varargCtor, args)
  }
}

object RegistryContext {
  def expression(varargCtor: Option[Constructor[_]], args: Array[AnyRef]): Expression = {
    if (varargCtor.isDefined) {
      Try(InstanceUtils.newInstance(varargCtor.get, args).asInstanceOf[Expression]) match {
        case Success(e) => e
        case Failure(e) =>
          // the exception is an invocation exception. To get a meaningful message, we need the cause.
          throw new IllegalArgumentException(e.getCause.getMessage)
      }
    } else {
      throw new IllegalArgumentException("Constructors error, varargCtor=" + varargCtor.mkString("\t") + ", args=" + args.mkString(","))
    }
  }

  def unapply(cxt: RegistryContext): Some[(String, Array[Any], Option[String])] = {
    Some(cxt.operatorName, cxt.operands, cxt.cacheKey)
  }

  def unapply2(cxt: RegistryContext): Some[(String, Array[Any])] = {
    Some(cxt.operatorName, cxt.operands)
  }

  def unapply(operatorName: String): RegistryContext = {
    RegistryContext(null, operatorName = operatorName, operands = null, getDataType = null)
  }
}
