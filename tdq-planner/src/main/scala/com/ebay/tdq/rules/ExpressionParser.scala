package com.ebay.tdq.rules

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.expressions.Expression
import com.ebay.tdq.rules.ProfilingSqlParser.{getExpr, getExprIdentifiers}
import com.ebay.tdq.types.DataType
import org.apache.avro.Schema
import org.apache.calcite.sql._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
 * @author juntzhang
 */
case class ExpressionParser(expression: String, tdqEnv: TdqEnv, schema: Schema) {

  import ProfilingSqlParser._

  private val expressionRegistry = ExpressionRegistry(tdqEnv, getDataType0(schema))

  private def transformSqlNode(sqlNode: SqlNode): Expression = {
    if (sqlNode == null) return null
    sqlNode match {
      case call: SqlBasicCall => transformSqlBaseCall(call)
      case literal: SqlLiteral => transformLiteral(literal)
      case identifier: SqlIdentifier => transformIdentifier(identifier)
      case _ => throw new IllegalStateException("Unexpected SqlCall: " + sqlNode)
    }
  }

  private def transformOperands(operands: Seq[SqlNode]): Array[Any] = {
    if (operands == null || operands.isEmpty) {
      return Array()
    }
    val ans = new Array[Any](operands.length)
    for (i <- operands.indices) {
      operands(i) match {
        case call: SqlBasicCall =>
          ans(i) = transformSqlBaseCall(call)
        case list: SqlNodeList =>
          val seq = new ArrayBuffer[Expression]
          list.asScala.foreach(node => {
            seq += transformLiteral(node.asInstanceOf[SqlLiteral])
          })
          ans(i) = seq
        case literal: SqlLiteral => ans(i) = transformLiteral(literal)
        case _: SqlIdentifier =>
          ans(i) = transformIdentifier(operands(i).asInstanceOf[SqlIdentifier])
        case spec: SqlDataTypeSpec => // find DateType
          ans(i) = DataType.nameToType(spec.getTypeName.getSimple)
        case _ => throw new IllegalStateException("Unexpected operand: " + operands(i))
      }
    }
    ans
  }

  private def transformIdentifier(identifier: SqlIdentifier): Expression = {
    transformIdentifier0(schema, identifier.toString)
  }

  private def transformSqlBaseCall(call: SqlBasicCall): Expression = {
    val operator = call.getOperator.getName
    expressionRegistry.parse(operator, transformOperands(call.getOperands), "")
  }

  def parse(): Expression = {
    val sqlNode = getExpr(expression)
    transformSqlNode(sqlNode)
  }
}

object ExpressionParser {
  def expr(expression: String, tdqEnv: TdqEnv, schemaType: Schema.Type = Schema.Type.DOUBLE): Expression = {
    val sqlNode = getExpr(expression)
    val identifiers = getExprIdentifiers(expression)
    val s = identifiers.map(i => (i.names.get(0), i.names.get(1))).groupBy(_._1).map {
      case (k, vs) =>
        val fields = vs.map(v => {
          s"""
             |{ "name": "${v._2}", "type": "${schemaType.getName}"}
             |""".stripMargin
        }).mkString(",")
        s"""
          |{
          |     "name": "$k",
          |     "type": {
          |        "type": "record",
          |        "name": "$k",
          |        "fields":[
          |           $fields
          |        ]
          |     }
          |    }
          |""".stripMargin
    }.mkString(",\n")

    val schema = new Schema.Parser().parse(
      s"""
         |{
         |  "type": "record",
         |  "name": "EvaluationRecord",
         |  "fields": [
         |    $s
         |  ]
         |}
         |""".stripMargin)
    println(schema)

    ExpressionParser(expression, tdqEnv, schema).transformSqlNode(sqlNode)
  }
}