package com.ebay.tdq.functions;

import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import lombok.SneakyThrows;
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
        return merge0(m1, m2);
    }

    @SneakyThrows
    @Override
    public TdqMetric getResult(TdqMetric m) {
        if (m == null) {
            return null;
        }
        ((PhysicalPlan) m.getPhysicalPlan()).evaluate(m);
        return m;
    }

    @Override
    public TdqMetric merge(TdqMetric m1, TdqMetric m2) {
        return merge0(m1, m2);
    }

    public TdqMetric merge0(TdqMetric m1, TdqMetric m2) {
        if (m1 == null && m2 == null) {
            return null;
        } else if (m1 == null) {
            return m2;
        } else if (m2 == null) {
            return m1;
        } else {
            return ((PhysicalPlan) m1.getPhysicalPlan()).merge(m1, m2);
        }
    }
}
