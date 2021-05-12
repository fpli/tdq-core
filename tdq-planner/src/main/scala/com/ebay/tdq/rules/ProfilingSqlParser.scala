package com.ebay.tdq.rules

import com.ebay.tdq.config.{ExpressionConfig, ProfilerConfig, TransformationConfig}
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.AggregateExpression
import com.ebay.tdq.types.DataType
import org.apache.calcite.avatica.util.{Casing, Quoting}
import org.apache.calcite.sql._
import org.apache.calcite.sql.fun.{SqlCase, SqlStdOperatorTable}
import org.apache.calcite.sql.parser.SqlParser
import org.apache.calcite.tools.Frameworks
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.StringUtils

import scala.collection.JavaConverters.iterableAsScalaIterableConverter
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
 * @author juntzhang
 */
object ProfilingSqlParser {
  private val FRAMEWORK_CONFIG = Frameworks
    .newConfigBuilder
    .parserConfig(
      SqlParser.configBuilder.setCaseSensitive(false)
        .setQuoting(Quoting.BACK_TICK)
        .setQuotedCasing(Casing.UNCHANGED)
        .setUnquotedCasing(Casing.UNCHANGED).build
    )
    .operatorTable(SqlStdOperatorTable.instance)
    .build

  def getSql(sql: String): SqlSelect = {
    val parser = SqlParser.create(sql, FRAMEWORK_CONFIG.getParserConfig)
    parser.parseStmt.asInstanceOf[SqlSelect]
  }

  def getExpr(str: String): SqlNode = {
    val parser = SqlParser.create("SELECT " + str, FRAMEWORK_CONFIG.getParserConfig)
    parser.parseStmt.asInstanceOf[SqlSelect].getSelectList.get(0)
  }

  def getExprIdentifiers(str: String): Array[SqlIdentifier] = {
    val parser = SqlParser.create("SELECT " + str, FRAMEWORK_CONFIG.getParserConfig)
    val buffer = new ArrayBuffer[SqlIdentifier]()
    parser.parseStmt.asInstanceOf[SqlSelect].getSelectList.get(0) match {
      case call: SqlBasicCall => baseCallIdentifiers(call, buffer)
      case sqlCase: SqlCase => sqlCaseIdentifiers(sqlCase, buffer)
      case identifier: SqlIdentifier => buffer += identifier
      case _ =>
    }
    buffer.toArray
  }

  private def baseCallIdentifiers(call: SqlBasicCall, buffer: ArrayBuffer[SqlIdentifier]): Unit = {
    call.operands.foreach {
      case c: SqlBasicCall =>
        baseCallIdentifiers(c, buffer)
      case list: SqlNodeList =>
        list.asScala.foreach {
          case ii: SqlIdentifier => buffer += ii
          case n: SqlBasicCall => baseCallIdentifiers(n, buffer)
        }
      case i: SqlIdentifier =>
        buffer += i
      case _ =>
    }
  }

  private def sqlCaseIdentifiers(sqlCase: SqlCase, buffer: ArrayBuffer[SqlIdentifier]): Unit = {
    for (i <- 0 until sqlCase.getWhenOperands.size) {
      baseCallIdentifiers(sqlCase.getWhenOperands.get(i).asInstanceOf[SqlBasicCall], buffer)
    }
  }

  def getFilterIdentifiers(filter: String): Array[SqlIdentifier] = {
    val buffer = new ArrayBuffer[SqlIdentifier]()
    baseCallIdentifiers(getSql("SELECT 1 FROM T where " + filter).getWhere.asInstanceOf[SqlBasicCall],buffer)
    buffer.toArray
  }
}

class ProfilingSqlParser(profilerConfig: ProfilerConfig, window: Long) {

  import ProfilingSqlParser._

  private lazy val dimensions = if (CollectionUtils.isNotEmpty(profilerConfig.getDimensions)) {
    profilerConfig.getDimensions.asScala.toSet
  } else {
    Set[String]()
  }


  private val aliasTransformationConfigMap = profilerConfig
    .getTransformations
    .asScala
    .filter(_.getAlias.nonEmpty)
    .map(transformationConfig => {
      transformationConfig.getAlias -> transformationConfig
    }).toMap
  private val expressionMap = new mutable.HashMap[String, Expression]

  def parsePlan(): PhysicalPlan = {
    val aggregations = profilerConfig.getTransformations.asScala.map { cfg =>
      val expr = parseTransformationPlan(cfg)
      if (expr.isInstanceOf[AggregateExpression]) {
        AggrPhysicalPlan(
          cfg.getAlias,
          parseFilter(cfg.getFilter),
          parseTransformationPlan(cfg)
        )
      } else {
        null
      }
    }.filter(_ != null).toList

    val dimArr = expressionMap.filter { case (k, _) => dimensions.contains(k) }.values.toArray
    if (dimArr.length != dimensions.size) {
      throw new IllegalArgumentException("dimensions config illegal!")
    }

    val evaluation = parseExpressionPlan(profilerConfig.getExpression, "")

    val plan = PhysicalPlan(
      metricKey = profilerConfig.getMetricName,
      window = window,
      filter = parseFilter(profilerConfig.getFilter),
      dimensions = dimArr,
      evaluation = evaluation,
      aggregations = aggregations.toArray
    )

    plan
  }


  // TODO need add more rule:
  //  1.Casts types according to the expected input types for [[Expression]]s. like spark ImplicitTypeCasts
  private def transformLiteral(literal: SqlLiteral): Expression = {
    literal match {
      case snl: SqlNumericLiteral =>
        // TODO: currently only support integer and double, you can explicit use cast coerce types
        //  if column1 is DECIMAL,`column1 > cast(10 as DECIMAL)`
        if (snl.isInteger) {
          Literal.apply(snl.getValueAs(classOf[Integer]))
        } else {
          Literal.apply(literal.getValueAs(classOf[Number]).doubleValue())
        }
      case _: SqlCharStringLiteral => Literal.apply(literal.getValueAs(classOf[String]))
      case _ => throw new IllegalStateException("Unexpected literal: " + literal)
    }
  }

  private def transformIdentifier(identifier: SqlIdentifier): Expression = {
    val name = identifier.toString
    val config = aliasTransformationConfigMap.get(name)
    if (config.isDefined) {
      var expression = expressionMap.get(name)
      if (expression.isDefined) expression.get
      else {
        // generate by other transformation
        expression = Some(parseTransformationPlan(config.get))
        expressionMap.put(config.get.getAlias, expression.get)
        expression.get
      }
    } else {
      // get from raw event todo data type
      GetStructField(name)
    }
  }

  private def transformOperands(operands: Seq[SqlNode]): Array[Any] = {
    if (operands == null || operands.isEmpty) {
      return Array()
    }
    val ans = new Array[Any](operands.length)
    for (i <- operands.indices) {
      operands(i) match {
        case c: SqlCase =>
          ans(i) = transformSqlCase(c, "")
        case call: SqlBasicCall =>
          ans(i) = transformSqlBaseCall(call, "")
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

  private def transformSqlNode(sqlNode: SqlNode, alias: String): Expression = {
    if (sqlNode == null) return null
    sqlNode match {
      case call: SqlBasicCall => transformSqlBaseCall(call, alias)
      case sqlCase: SqlCase => transformSqlCase(sqlCase, alias)
      case literal: SqlLiteral => transformLiteral(literal)
      case identifier: SqlIdentifier => transformIdentifier(identifier)
      case _ => throw new IllegalStateException("Unexpected SqlCall: " + sqlNode)
    }
  }

  private def transformSqlBaseCall(call: SqlBasicCall, alias: String): Expression = {
    val operator = call.getOperator.getName
    val expression = ExpressionRegistry.parse(operator, transformOperands(call.getOperands), alias)
    if (StringUtils.isNotBlank(alias)) expressionMap.put(alias, expression)
    expression
  }

  private def transformSqlCase(sqlCase: SqlCase, alias: String): CaseWhen = {
    val buffer = new ArrayBuffer[(Expression, Expression)]
    for (i <- 0 until sqlCase.getWhenOperands.size) {
      buffer += ((
        transformSqlBaseCall(sqlCase.getWhenOperands.get(i).asInstanceOf[SqlBasicCall], ""),
        transformLiteral(sqlCase.getThenOperands.get(i).asInstanceOf[SqlLiteral])
      ))
    }
    val expression = new CaseWhen(
      buffer,
      Some(transformLiteral(sqlCase.getElseOperand.asInstanceOf[SqlLiteral])),
      cacheKey = Some(alias).filter(StringUtils.isNotBlank)
    )
    if (StringUtils.isNotBlank(alias)) expressionMap.put(alias, expression)
    expression
  }

  private def parseExpressionPlan(cfg: ExpressionConfig, alias: String): Expression = {
    val operator = cfg.getOperator
    if (operator.equalsIgnoreCase("UDF") || operator.equalsIgnoreCase("Expr")) {
      val sqlNode = getExpr(cfg.getConfig.get("text").asInstanceOf[String])
      transformSqlNode(sqlNode, alias)
    }
    else transformSqlNode(getExpr(operator + "(" + cfg.getConfig.get("arg0") + ")"), alias)
  }

  private def parseTransformationPlan(cfg: TransformationConfig): Expression = {
    val alias = cfg.getAlias
    parseExpressionPlan(cfg.getExpression, alias)
  }

  private def parseFilter(filter: String): Expression = {
    if (StringUtils.isNotBlank(filter)) {
      transformSqlBaseCall(getSql("SELECT 1 FROM T where " + filter)
        .getWhere.asInstanceOf[SqlBasicCall], "")
    } else {
      null
    }
  }
}
