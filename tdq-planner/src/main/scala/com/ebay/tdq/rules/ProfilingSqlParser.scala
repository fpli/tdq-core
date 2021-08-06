package com.ebay.tdq.rules

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.config.{ExpressionConfig, ProfilerConfig, TransformationConfig}
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.AggregateExpression
import com.ebay.tdq.types._
import org.apache.avro.Schema
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
    if (StringUtils.isBlank(str)) {
      return null
    }
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

  def getFilterIdentifiers(filter: String): Array[SqlIdentifier] = {
    val buffer = new ArrayBuffer[SqlIdentifier]()
    baseCallIdentifiers(getSql("SELECT 1 FROM T where " + filter).getWhere.asInstanceOf[SqlBasicCall], buffer)
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
}

class ProfilingSqlParser(profilerConfig: ProfilerConfig, window: Long, tdqEnv: TdqEnv, schema: Schema) {

  import ProfilingSqlParser._

  lazy val physicalPlanContext: Option[PhysicalPlanContext] = {
    if (profilerConfig.getConfig == null) {
      None
    } else {
      Some(PhysicalPlanContext(
        sampling = profilerConfig.getSampling,
        samplingFraction = profilerConfig.getSamplingFraction,
        prontoDropdownExpr = profilerConfig.getProntoDropdownExpr,
        prontoFilterExpr = profilerConfig.getProntoFilterExpr
      ))
    }
  }
  private val expressionRegistry = ExpressionRegistry(tdqEnv, this)
  private val aliasTransformationConfigMap = profilerConfig
    .getTransformations
    .asScala
    .filter(_.getAlias.nonEmpty)
    .map(transformationConfig => {
      transformationConfig.getAlias -> transformationConfig
    }).toMap
  private val expressionMap = new mutable.HashMap[String, Expression]

  def parseProntoFilterExpr(): SqlNode = {
    if (physicalPlanContext.isDefined && StringUtils.isNotBlank(physicalPlanContext.get.prontoFilterExpr)) {
      val expr = physicalPlanContext.get.prontoFilterExpr
      getExpr(expr)
    } else {
      null
    }
  }

  def parseProntoDropdownExpr(): SqlNode = {
    if (physicalPlanContext.isDefined && StringUtils.isNotBlank(physicalPlanContext.get.prontoDropdownExpr)) {
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
      val dimensionSet = profilerConfig.getDimensions.asScala.toSet
      val ans = transformations.filter(t => dimensionSet.contains(t.name)).toArray
      val trans = ans.map(_.name).toSet

      // dimensions are directly fields in TdqEvent
      dimensionSet.diff(trans).map(name => {
        Transformation(name, null, transformIdentifier(name))
      }).toArray ++ ans
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

  def getType(f: Schema): DataType = {
    f.getType match {
      case Schema.Type.INT => IntegerType
      case Schema.Type.LONG => LongType
      case Schema.Type.DOUBLE => DoubleType
      case Schema.Type.FLOAT => FloatType
      case Schema.Type.BOOLEAN => BooleanType
      case Schema.Type.STRING => StringType
      case Schema.Type.MAP => MapType(StringType, getType(f.getValueType))
      case Schema.Type.UNION => getType(f.getTypes.asScala.filter(_.getType != Schema.Type.NULL).head)
      case _ =>
        throw new IllegalArgumentException(s"${f.getType} not implement!")
    }
  }

  def getDataType(name: String): DataType = {
    val f = TdqEvent.getField(schema, name)
    if (f == null) {
      return com.ebay.tdq.types.StringType
    }
    getType(f)
  }

  def getDataType0(name: Array[String]): DataType = {
    val f = TdqEvent.getField0(schema, name)
    if (f == null) {
      return com.ebay.tdq.types.StringType
    }
    getType(f)
  }

  private def transformIdentifier(name: String): Expression = {
    name match {
      case ci"EVENT_TIMESTAMP" =>
        GetTdqField("event_timestamp", TimestampType)
      case ci"EVENT_TIME_MILLIS" =>
        GetTdqField("event_time_millis", LongType)
      case ci"SOJ_TIMESTAMP" =>
        GetTdqField("soj_timestamp", LongType)
      case _ =>
        GetTdqField(name, getDataType(name))
    }
  }

  private def transformIdentifier(identifier: SqlIdentifier): Expression = {
    val name = identifier.toString
    val config = aliasTransformationConfigMap.get(name)
    var expression = expressionMap.get(name)
    if (config.isDefined && expression.isDefined) {
      expression.get
    } else if (config.isDefined && expression.isEmpty && !config.get.getExpr.equals(config.get.getAlias)) {
      // if expression(identifier) is same as alias
      // generate by other transformation
      expression = Some(parseTransformationPlan(config.get))
      expressionMap.put(config.get.getAlias, expression.get)
      expression.get
    } else {
      transformIdentifier(name)
    }
  }

  implicit class CaseInsensitiveRegex(sc: StringContext) {
    def ci: Regex = ("(?i)" + sc.parts.mkString).r
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
    val expression = expressionRegistry.parse(operator, transformOperands(call.getOperands), alias)
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
