package com.ebay.tdq.sample;

import com.ebay.tdq.rules.TdqMetric;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @author juntzhang
 */
public class TdqAggregateFunction implements AggregateFunction<TdqMetric, TdqMetric, TdqMetric> {
    @Override
    public TdqMetric createAccumulator() {
        return null;
    }

    @Override
    public TdqMetric add(TdqMetric m1, TdqMetric m2) {
        if (m1 == null) {
            return m2;
        } else if (m2 == null) {
            return m1;
        } else {
            return merge(m1, m2);
        }
    }

    @SneakyThrows
    @Override
    public TdqMetric getResult(TdqMetric m) {
        SqlNode node =
                TdqRawEventProcessFunction
                        .getExpr((String) m.getProfilerConfig().getExpression().getConfig().get(
                                "text"));
        Object val;
        if (node instanceof SqlBasicCall) {
            // math calculator
            SqlBasicCall call = (SqlBasicCall) node;
            val = ExprFunctions.opt(call.getOperator().getName(),
                    TdqRawEventProcessFunction
                            .transformOperands(call.getOperands(), m.getExprMap()));
        } else if (node instanceof SqlIdentifier) {
            // select from pre agg
            val = m.getExprMap().get(((SqlIdentifier) node).getSimple());
        } else {
            throw new IllegalStateException("Unexpected operand: " + node);
        }
        m.setValue((Double) val);
        return m;
    }

    @Override
    public TdqMetric merge(TdqMetric m1, TdqMetric m2) {
        Set<String> keys = new HashSet<>(m1.getExprMap().keySet());
        TdqMetric   m    = m1.copy();
        if (m.getEventTime() < m2.getEventTime()) {
            m.setEventTime(m2.getEventTime());
        }
        keys.addAll(m2.getExprMap().keySet());
        for (String k : keys) {
            m.getExprMap().put(
                    k,
                    (Double) m1.getExprMap().getOrDefault(k, 0d)
                            + (Double) m2.getExprMap().getOrDefault(k, 0d)
            );
        }
        return m;
    }
}
