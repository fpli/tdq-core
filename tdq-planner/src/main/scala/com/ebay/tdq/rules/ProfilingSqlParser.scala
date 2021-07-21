package com.ebay.tdq.rules

import com.ebay.tdq.config.{ExpressionConfig, ProfilerConfig, TransformationConfig}
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.AggregateExpression
import com.ebay.tdq.types.{DataType, TimestampType}
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
import scala.util.matching.Regex


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
          case _: SqlLiteral => // ignore
        }
      case i: SqlIdentifier =>
        buffer += i
      case _ =>
    }
  }

  // TODO need add more rule:
  //  1.Casts types according to the expected input types for [[Expression]]s. like spark `ImplicitTypeCasts`
  private def transformLiteral(literal: SqlLiteral): Expression = {
    literal match {
      case snl: SqlNumericLiteral =>
        if (snl.isInteger) {
          val v = snl.getValueAs(classOf[Number]).longValue()
          if (v > Integer.MIN_VALUE && v < Integer.MAX_VALUE) {
            Literal.apply(v.toInt)
          } else {
            Literal.apply(v)
          }
        } else {
          Literal.apply(literal.getValueAs(classOf[Number]).doubleValue())
        }
      case _: SqlCharStringLiteral => Literal.apply(literal.getValueAs(classOf[String]))
      case _ => throw new IllegalStateException("Unexpected literal: " + literal)
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

  private val aliasTransformationConfigMap = profilerConfig
    .getTransformations
    .asScala
    .filter(_.getAlias.nonEmpty)
    .map(transformationConfig => {
      transformationConfig.getAlias -> transformationConfig
    }).toMap
  private val expressionMap = new mutable.HashMap[String, Expression]
  lazy val physicalPlanContext: Option[PhysicalPlanContext] = {
    if (profilerConfig.getConfig == null) {
      None
    } else {
      Some(PhysicalPlanContext(
        sampling = profilerConfig.getSampling,
        samplingFraction = profilerConfig.getSamplingFraction,
        prontoDropdownExpr = profilerConfig.getProntoDropdownExpr
      ))
    }
  }

  def parseProntoSqlNode(): SqlNode = {
    if (physicalPlanContext.isDefined) {
      val expr = physicalPlanContext.get.prontoDropdownExpr
      getExpr(expr)
    } else {
      null
    }
  }

  def parsePlan(): PhysicalPlan = {
    val transformations = profilerConfig.getTransformations.asScala.map { cfg =>
      Transformation(
        cfg.getAlias,
        parseFilter(cfg.getFilter),
        parseTransformationPlan(cfg)
      )
    }

    val dimensions = if (CollectionUtils.isNotEmpty(profilerConfig.getDimensions)) {
      val set = profilerConfig.getDimensions.asScala.toSet
      val ans = transformations.filter(t => {
        set.contains(t.name)
      }).toArray
      if (ans.length != set.size) {
        throw new IllegalArgumentException("dimensions config illegal!")
      }
      ans
    } else {
      Array[Transformation]()
    }

    val evaluation = if (StringUtils.isNotBlank(profilerConfig.getExpr)) {
      parseExpressionPlan(profilerConfig.getExpr, "")
    } else {
      parseExpressionPlan(profilerConfig.getExpression, "")
    }

    val plan = PhysicalPlan(
      metricKey = profilerConfig.getMetricName,
      window = window,
      filter = parseFilter(profilerConfig.getFilter),
      dimensions = dimensions,
      evaluation = evaluation,
      aggregations = transformations.filter(_.expr.isInstanceOf[AggregateExpression]).toArray,
      cxt = physicalPlanContext
    )
    plan
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
      name match {
        case ci"EVENT_TIMESTAMP" =>
          TdqTimestamp("event_timestamp", TimestampType)
        case ci"EVENT_TIME_MILLIS" =>
          TdqTimestamp()
        case ci"SOJ_TIMESTAMP" =>
          TdqTimestamp("soj_timestamp")
        case _ =>
          GetStructField(name)
      }
    }
  }
  implicit class CaseInsensitiveRegex(sc: StringContext) {
    def ci: Regex = ( "(?i)" + sc.parts.mkString ).r
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

  private def parseExpressionPlan(exprStr: String, alias: String): Expression = {
    val expr = transformSqlNode(getExpr(exprStr), alias)
    if (!expressionMap.isDefinedAt(alias) && StringUtils.isNotBlank(alias)) {
      expressionMap.put(alias, expr)
    }
    expr
  }

  private def parseExpressionPlan(cfg: ExpressionConfig, alias: String): Expression = {
    val operator = cfg.getOperator
    val expr = if (
        operator.equalsIgnoreCase("UDAF") ||
        operator.equalsIgnoreCase("UDF") ||
        operator.equalsIgnoreCase("Expr")) {
      val sqlNode = getExpr(cfg.getConfig.get("text").asInstanceOf[String])
      transformSqlNode(sqlNode, alias)
    } else if (cfg.getConfig.get("arg0") != null) {
      transformSqlNode(getExpr(operator + "(" + cfg.getConfig.get("arg0") + ")"), alias)
    } else {
      throw new IllegalStateException("Unexpected operator: " + operator)
    }

    if (!expressionMap.isDefinedAt(alias) && StringUtils.isNotBlank(alias)) {
      expressionMap.put(alias, expr)
    }
    expr
  }

  private def parseTransformationPlan(cfg: TransformationConfig): Expression = {
    val alias = cfg.getAlias
    if (StringUtils.isNotBlank(cfg.getExpr)) {
      parseExpressionPlan(cfg.getExpr, alias)
    } else {
      parseExpressionPlan(cfg.getExpression, alias)
    }
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
