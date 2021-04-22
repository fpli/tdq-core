//package com.ebay.tdq.rules;
//
//import com.ebay.tdq.config.ExpressionConfig;
//import com.ebay.tdq.config.ProfilerConfig;
//import com.ebay.tdq.config.TransformationConfig;
//import com.ebay.tdq.rules.expressions.CaseWhen;
//import com.ebay.tdq.rules.expressions.DataType;
//import com.ebay.tdq.rules.expressions.Expression;
//import com.ebay.tdq.rules.expressions.GetStructField;
//import com.ebay.tdq.rules.expressions.Literal;
//import com.ebay.tdq.rules.expressions.aggregate.AggregateExpression;
//import com.ebay.tdq.rules.physical.AggrPhysicalPlan;
//import com.ebay.tdq.rules.physical.PhysicalPlan;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.calcite.avatica.util.Casing;
//import org.apache.calcite.avatica.util.Quoting;
//import org.apache.calcite.sql.SqlBasicCall;
//import org.apache.calcite.sql.SqlDataTypeSpec;
//import org.apache.calcite.sql.SqlIdentifier;
//import org.apache.calcite.sql.SqlLiteral;
//import org.apache.calcite.sql.SqlNode;
//import org.apache.calcite.sql.SqlNodeList;
//import org.apache.calcite.sql.SqlSelect;
//import org.apache.calcite.sql.fun.SqlCase;
//import org.apache.calcite.sql.fun.SqlStdOperatorTable;
//import org.apache.calcite.sql.parser.SqlParseException;
//import org.apache.calcite.sql.parser.SqlParser;
//import org.apache.calcite.tools.FrameworkConfig;
//import org.apache.calcite.tools.Frameworks;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @author juntzhang
// */
//@Data
//@Slf4j
//public class ExpressionSqlParser {
//    private static final FrameworkConfig FRAMEWORK_CONFIG = Frameworks
//            .newConfigBuilder()
//            .parserConfig(SqlParser.configBuilder()
//                    .setCaseSensitive(false)
//                    .setQuoting(Quoting.BACK_TICK)
//                    .setQuotedCasing(Casing.UNCHANGED)
//                    .setUnquotedCasing(Casing.UNCHANGED)
//                    .build()
//            )
//            .operatorTable(SqlStdOperatorTable.instance())
//            .build();
//
//    private Map<String, TransformationConfig> aliasTransformationConfigMap =
//            new HashMap<>();
//    private Map<String, Expression>           expressionMap                =
//            new HashMap<>();
//    private ProfilerConfig                    profilerConfig;
//    private Long                              window;
//
//    public ExpressionSqlParser(ProfilerConfig profilerConfig, Long window) {
//        this.profilerConfig = profilerConfig;
//        this.window         = window;
//    }
//
//
//    private static SqlSelect getSql(String sql) throws SqlParseException {
//        SqlParser parser = SqlParser.create(sql, FRAMEWORK_CONFIG.getParserConfig());
//        return (SqlSelect) parser.parseStmt();
//    }
//
//    private static SqlNode getExpr(String str) throws SqlParseException {
//        SqlParser parser = SqlParser.create("SELECT " + str, FRAMEWORK_CONFIG.getParserConfig());
//        return ((SqlSelect) parser.parseStmt()).getSelectList().get(0);
//    }
//
//    private Literal transformLiteral(SqlLiteral literal) {
//        return Literal.apply(literal.getValue());
//    }
//
//
//    private Object[] transformOperands(SqlNode[] operands) throws SqlParseException {
//        if (operands == null || operands.length == 0) {
//            return new Object[]{};
//        }
//        Object[] ans = new Object[operands.length];
//        for (int i = 0; i < operands.length; i++) {
//            if (operands[i] instanceof SqlBasicCall) {
//                SqlBasicCall call = (SqlBasicCall) operands[i];
//                ans[i] = transformSqlBaseCall(call, "");
//            } else if (operands[i] instanceof SqlNodeList) {
//                scala.collection.mutable.ArrayBuffer<Expression>
//                        seq = new scala.collection.mutable.ArrayBuffer<>();
//                for (SqlNode node : ((SqlNodeList) operands[i])) {
//                    seq.$plus$eq(transformLiteral((SqlLiteral) node));
//                }
//                ans[i] = seq;
//            } else if (operands[i] instanceof SqlLiteral) {
//                ans[i] = transformLiteral((SqlLiteral) operands[i]);
//            } else if (operands[i] instanceof SqlIdentifier) {
//                String               name   = operands[i].toString();
//                TransformationConfig config = aliasTransformationConfigMap.get(name);
//                if (config != null) {
//                    Expression expression = expressionMap.get(name);
//                    if (expression != null) {
//                        ans[i] = expression;
//                    } else {
//                        // generate by other transformation
//                        expression = parseTransformationPlan(config);
//                        ans[i]     = expression;
//                        expressionMap.put(config.getAlias(), expression);
//                    }
//                } else {
//                    // get from raw event
//                    ans[i] = new GetStructField(name);
//                }
//            } else if (operands[i] instanceof SqlDataTypeSpec) {
//                // find DateType
//                ans[i] =
//                        DataType.nameToType(((SqlDataTypeSpec) operands[i]).getTypeName()
//                        .getSimple());
//            } else {
//                throw new IllegalStateException("Unexpected operand: " + operands[i]);
//            }
//        }
//        return ans;
//    }
//
//    private Expression transformSqlNode(SqlNode sqlNode, String alias) throws SqlParseException {
//        if (sqlNode == null) {
//            return null;
//        }
//        if (sqlNode instanceof SqlBasicCall) {
//            return (transformSqlBaseCall(((SqlBasicCall) sqlNode), alias));
//        } else if (sqlNode instanceof SqlCase) {
//            return (transformSqlCase((SqlCase) sqlNode, alias));
//        } else {
//            throw new IllegalStateException("Unexpected SqlCall: " + sqlNode);
//        }
//    }
//
//    private Expression transformSqlBaseCall(SqlBasicCall call, String alias) throws
//    SqlParseException {
//        String operator = call.getOperator().getName();
//        Expression expression = ExpressionRegistry.parse(operator,
//                transformOperands(call.getOperands()), alias);
//        if (StringUtils.isNotBlank(alias)) {
//            expressionMap.put(alias, expression);
//        }
//        return expression;
//    }
//
//
//    private Expression transformSqlCase(SqlCase sqlCase, String alias) throws SqlParseException {
//        scala.collection.mutable.ArrayBuffer<scala.Tuple2<Expression, Expression>>
//                seq = new scala.collection.mutable.ArrayBuffer<>();
//        for (int i = 0; i < sqlCase.getWhenOperands().size(); i++) {
//            scala.Tuple2<Expression, Expression> tuple2 = new scala.Tuple2<Expression,
//            Expression>(
//                    transformSqlBaseCall((SqlBasicCall) sqlCase.getWhenOperands().get(i), ""),
//                    transformLiteral((SqlLiteral) sqlCase.getThenOperands().get(i))
//            );
//            seq.$plus$eq(tuple2);
//        }
//        Expression expression = new CaseWhen(seq,
//                transformLiteral((SqlLiteral) sqlCase.getElseOperand()));
//        if (StringUtils.isNotBlank(alias)) {
//            expressionMap.put(alias, expression);
//        }
//        return expression;
//    }
//
//    private Expression parseExpressionPlan(ExpressionConfig cfg, String alias) throws
//    SqlParseException {
//        String operator = cfg.getOperator();
//        if (operator.equalsIgnoreCase("UDF")
//                || operator.equalsIgnoreCase("Expr")) {
//            SqlNode sqlNode = getExpr((String) cfg.getConfig().get("text"));
//            if (sqlNode instanceof SqlBasicCall) {
//                return transformSqlBaseCall(((SqlBasicCall) sqlNode), alias);
//            } else if (sqlNode instanceof SqlCase) {
//                return transformSqlCase((SqlCase) sqlNode, alias);
//            } else {
//                throw new IllegalStateException("Unexpected SqlCall: " + sqlNode);
//            }
//        } else {
//            return transformSqlNode(getExpr(operator + "(" + cfg.getConfig().get(
//                    "arg0") + ")"), alias);
//        }
//    }
//
//    private Expression parseTransformationPlan(TransformationConfig cfg) throws
//    SqlParseException {
//        String alias = cfg.getAlias();
//        return parseExpressionPlan(cfg.getExpression(), alias);
//    }
//
//    public PhysicalPlan parsePlan() throws SqlParseException {
//        PhysicalPlan plan = new PhysicalPlan();
//        if (StringUtils.isNotBlank(profilerConfig.getFilter())) {
//            plan.setFilter(transformSqlBaseCall(
//                    (SqlBasicCall) getSql("SELECT 1 FROM T where "
//                            + profilerConfig.getFilter()).getWhere(), ""));
//        }
//        plan.setWindow(window);
//        for (TransformationConfig cfg : profilerConfig.getTransformations()) {
//            Expression expression = parseTransformationPlan(cfg);
//            if (expression instanceof AggregateExpression) {
//                AggrPhysicalPlan aggrPhysicalPlan = new AggrPhysicalPlan();
//                if (StringUtils.isNotBlank(cfg.getFilter())) {
//                    aggrPhysicalPlan.setFilter(transformSqlBaseCall(
//                            (SqlBasicCall) getSql("SELECT 1 FROM T where " + cfg.getFilter())
//                            .getWhere(),
//                            ""));
//                }
//                aggrPhysicalPlan.setEvaluation(parseTransformationPlan(cfg));
//            }
//        }
//        plan.setEvaluation(parseExpressionPlan(profilerConfig.getExpression(), ""));
//        return plan;
//    }
//}
