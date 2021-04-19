package com.ebay.tdq.rules;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.config.TransformationConfig;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlNumericLiteral;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlCase;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqRawEventProcessFunction extends BroadcastProcessFunction<RawEvent, TdqConfig, TdqMetric> {
    private final MapStateDescriptor<String, TdqConfig> stateDescriptor;

    public TdqRawEventProcessFunction(MapStateDescriptor<String, TdqConfig> descriptor) {
        this.stateDescriptor = descriptor;
    }

    private static final FrameworkConfig config = Frameworks.newConfigBuilder()
            .parserConfig(SqlParser.configBuilder()
                    .setCaseSensitive(false)
                    .setQuoting(Quoting.BACK_TICK)
                    .setQuotedCasing(Casing.UNCHANGED)
                    .setUnquotedCasing(Casing.UNCHANGED)
                    .build()).operatorTable(SqlStdOperatorTable.instance())
            .build();

    private static SqlSelect getSql(String sql) throws SqlParseException {
        SqlParser parser = SqlParser.create(sql, config.getParserConfig());
        return (SqlSelect) parser.parseStmt();
    }

    public static SqlNode getExpr(String str) throws SqlParseException {
        SqlParser parser = SqlParser.create("SELECT " + str, config.getParserConfig());
        return ((SqlSelect) parser.parseStmt()).getSelectList().get(0);
    }

    public static String mapToString(Map<String, ?> map) {
        StringJoiner  sj          = new StringJoiner(",");
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            sj.add(key + "=" + map.get(key)+"\n");
        }
        mapAsString.append(sj).append("}");
        return mapAsString.toString();
    }

    public static Object transformLiteral(SqlLiteral literal) {
        if (literal instanceof SqlNumericLiteral) {
            return literal.getValueAs(Number.class);
        } else if (literal instanceof SqlCharStringLiteral) {
            return literal.getValueAs(String.class);
        } else {
            throw new IllegalStateException("Unexpected literal: " + literal);
        }
    }

    public static Object[] transformOperands(SqlNode[] operands, Map<String, Object> params) {
        if (operands == null || operands.length == 0) {
            return new Object[]{};
        }
        Object[] ans = new Object[operands.length];
        for (int i = 0; i < operands.length; i++) {
            if (operands[i] instanceof SqlBasicCall) {
                SqlBasicCall call = (SqlBasicCall) operands[i];
                ans[i] = ExprFunctions.opt(call.getOperator().getName(), transformOperands(call.operands, params));
            } else if (operands[i] instanceof SqlNodeList) {
                Set set = new HashSet();
                for (SqlNode node : ((SqlNodeList) operands[i])) {
                    set.add(transformLiteral((SqlLiteral) node));
                }
                ans[i] = set;
            } else if (operands[i] instanceof SqlLiteral) {
                ans[i] = transformLiteral((SqlLiteral) operands[i]);
            } else if (operands[i] instanceof SqlIdentifier) {
                // todo identifier with beans and mid fields
                String fieldName = operands[i].toString();
                Object o         = params.get(fieldName);
                if (o == null) {
                    try {
                        o = PropertyUtils.getProperty(params.get("__RAW_EVENT"), fieldName);
                    } catch (Exception e) {
//                        LOG.warn(e.getMessage());
                    }
                }
                ans[i] = o;
            } else if (operands[i] instanceof SqlDataTypeSpec) {
                Object o = ((SqlDataTypeSpec) operands[i]).getTypeName().getSimple();
                ans[i] = o;
            } else {
                throw new IllegalStateException("Unexpected operand: " + operands[i]);
            }
        }
        return ans;
    }

    public static Object sqlCase(TdqMetric m, SqlCase sqlCase, Map<String, Object> params) {
        for (int i = 0; i < sqlCase.getWhenOperands().size(); i++) {
            if ((Boolean) transformExpr0(m, (SqlBasicCall) sqlCase.getWhenOperands().get(i), params)) {
                return transformLiteral((SqlLiteral) sqlCase.getThenOperands().get(i));
            }
        }
        return transformLiteral((SqlLiteral) sqlCase.getElseOperand());
    }

    public static Object[] as(SqlBasicCall call) {
        if (call.getOperator().getName().equalsIgnoreCase("AS")) {
            Object[] ans = new Object[2];
            ans[0] = call.getOperands()[0];
            ans[1] = ((SqlIdentifier) call.getOperands()[1]).getSimple();
            return ans;
        } else {
            throw new IllegalStateException("Unexpected operator: " + call);
        }
    }

    public static void transformAggrExpr(TdqMetric m, String sql, Map<String, Object> params) throws SqlParseException {
        SqlSelect call     = getSql(sql);
        Object[]  pair     = as((SqlBasicCall) call.getSelectList().get(0));
        String    operator = ((SqlBasicCall) pair[0]).getOperator().getName();
        String    key      = (String) pair[1];
        double    val      = 1d;
        if (operator.equalsIgnoreCase("Count")) {
            if (call.getWhere() != null) {
                SqlBasicCall p = (SqlBasicCall) call.getWhere();
                if ((Boolean) ExprFunctions.opt(p.getOperator().getName(), transformOperands(p.operands, params))) {
                    m.putExpr(key, val);
                }
            } else {
                m.putExpr(key, 1d);
            }
        } else if (operator.equalsIgnoreCase("Sum")) {
            if (call.getWhere() != null) {
                SqlBasicCall p = (SqlBasicCall) call.getWhere();
                if ((Boolean) ExprFunctions.opt(p.getOperator().getName(), transformOperands(p.operands, params))) {
                    val = ((Number) transformOperands(((SqlBasicCall) pair[0]).getOperands(), params)[0]).doubleValue();
                    m.putExpr(key, val);
                }
            } else {
                val = ((Number) transformOperands(((SqlBasicCall) pair[0]).getOperands(), params)[0]).doubleValue();
                m.putExpr(key, val);
            }
        } else {
            throw new IllegalStateException("Unexpected operator: " + call);
        }
    }

    public static Object transformExpr0(TdqMetric m, SqlBasicCall call, Map<String, Object> params) {
        String operator = call.getOperator().getName();
        return ExprFunctions.opt(operator, transformOperands(call.getOperands(), params));
    }

    public static void transformExpr(TdqMetric m, ProfilerConfig profilerConfig, String expr,
            Map<String, Object> params) throws SqlParseException {
        Object[] pair = as((SqlBasicCall) getExpr(expr));
        String   key  = (String) pair[1];
        Object   val;
        if (pair[0] instanceof SqlBasicCall) {
            SqlBasicCall call     = ((SqlBasicCall) pair[0]);
            String       operator = call.getOperator().getName();
            val = ExprFunctions.opt(operator, transformOperands(call.getOperands(), params));
        } else if (pair[0] instanceof SqlCase) {
            val = sqlCase(m, (SqlCase) pair[0], params);
        } else {
            throw new IllegalStateException("Unexpected SqlCall: " + pair[0]);
        }

        params.put(key, val);
        if (CollectionUtils.isNotEmpty(profilerConfig.getDimensions()) &&
                profilerConfig.getDimensions().stream().anyMatch(p -> p.equalsIgnoreCase(key))) {
            m.putTag(key, val);
        }
    }

    public static String filter(String f1, String f2) {
        if (StringUtils.isNotBlank(f1) && StringUtils.isNotBlank(f2)) {
            return "(" + f1 + ") AND (" + f2 + ")";
        } else if (StringUtils.isNotBlank(f1)) {
            return f1;
        } else {
            return f2;
        }
    }

    public static void transform(TdqMetric m, ProfilerConfig profilerConfig,
            Map<String, Object> params) throws SqlParseException {
        // TODO depends
        for (TransformationConfig cfg : profilerConfig.getTransformations()) {
            String expr;
            String operator = cfg.getExpression().getOperator();
            String filter   = filter(profilerConfig.getFilter(), cfg.getFilter());
            if (operator.equalsIgnoreCase("UDF") || operator.equalsIgnoreCase("Expr")) {
                expr = cfg.getExpression().getConfig().get("text") + " AS " + cfg.getAlias();
                transformExpr(m, profilerConfig, expr, params);
            } else if (operator.equalsIgnoreCase("Count")) {
                String sql = "select Count(1) AS " + cfg.getAlias() + " FROM T";
                if (StringUtils.isNotBlank(filter)) {
                    sql += " where " + filter;
                }
                transformAggrExpr(m, sql, params);
            } else if (operator.equalsIgnoreCase("Sum")) {
                String sql =
                        "select Sum(" + cfg.getExpression().getConfig().get("arg0") + ") AS " + cfg.getAlias() + " " +
                                "FROM T";
                if (StringUtils.isNotBlank(filter)) {
                    sql += " where " + filter;
                }
                transformAggrExpr(m, sql, params);
            } else {
                throw new IllegalStateException("Unexpected expression: " + cfg);
            }
        }
    }

    public void processElement0(RawEvent rawEvent, ReadOnlyContext context, RuleConfig ruleConfig,
            Collector<TdqMetric> collector) throws SqlParseException {
        for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
            String              metricKey = profilerConfig.getMetricName();
            Map<String, Object> params    = new HashMap<>();
            params.put("__RAW_EVENT", rawEvent);
            TdqMetric m = new TdqMetric(metricKey, rawEvent.getEventTimestamp());
            transform(m, profilerConfig, params);
            m.setProfilerConfig(profilerConfig);
            m.genUID();
            if (m.getTags().size() != profilerConfig.getDimensions().size()) {
                log.warn("tags is illegal __RAW_EVENT[" + rawEvent + "]");
                return;
            }
            // get filter field first
            if (StringUtils.isNotBlank(profilerConfig.getFilter())) {
                SqlSelect    call = getSql("SELECT 1 FROM T WHERE " + profilerConfig.getFilter());
                SqlBasicCall p    = (SqlBasicCall) call.getWhere();
                if ((Boolean) ExprFunctions.opt(p.getOperator().getName(), transformOperands(p.operands, params))) {
                    collect(ruleConfig, collector, m);
                }
            } else {
                collect(ruleConfig, collector, m);
            }
        }
    }

    private void collect(RuleConfig ruleConfig, Collector<TdqMetric> collector, TdqMetric m) {
        m.setWindow(DateUtils.toSeconds((String) ruleConfig.getConfig().get("window")));
        if (m.getWindow() == null) {
            // TODO add metric monitor
            log.warn("Rule config[{}] illegal, missing window!", ruleConfig);
        } else {
            collector.collect(m);
        }
    }

    @Override
    public void processElement(RawEvent rawEvent, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
        ReadOnlyBroadcastState<String, TdqConfig> broadcastState = ctx.getBroadcastState(stateDescriptor);
        boolean                                   success        = false;
        for (Map.Entry<String, TdqConfig> entry : broadcastState.immutableEntries()) {
            try {
                for (RuleConfig ruleConfig : entry.getValue().getRules()) {
                    processElement0(rawEvent, ctx, ruleConfig, collector);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            success = true;
        }
        if (!success) {
            log.warn("Drop events {}", rawEvent);
        }
    }

    @Override
    public void processBroadcastElement(TdqConfig tdqConfig, Context ctx, Collector<TdqMetric> collector) throws Exception {
        BroadcastState<String, TdqConfig> broadcastState = ctx.getBroadcastState(stateDescriptor);
        broadcastState.put(tdqConfig.getId(), tdqConfig);
    }


}
