package com.ebay.tdq.functions;

import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.rules.ExpressionRegistry;
import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @author juntzhang
 */
public class TdqMetricAggregateFunction implements AggregateFunction<InternalMetric, InternalMetric, InternalMetric> {

  public static InternalMetric merge0(InternalMetric m1, InternalMetric m2) {
    if (m1 == null && m2 == null) {
      return null;
    } else if (m1 == null) {
      return m2;
    } else if (m2 == null) {
      return m1;
    } else {
      m1.getAggrExpresses().putAll(m2.getAggrExpresses());
      m1.getAggrExpresses().forEach((expr, operatorName) -> {
        Double newV = ExpressionRegistry.aggregateOperator(
            operatorName,
            m1.getValues().getOrDefault(expr, 0d),
            m2.getValues().getOrDefault(expr, 0d)
        );
        m1.putExpr(expr, newV);
      });
      return m1;
    }
  }

  @Override
  public InternalMetric createAccumulator() {
    return null;
  }

  @Override
  public InternalMetric add(InternalMetric m1, InternalMetric m2) {
    return merge0(m1, m2);
  }

  @Override
  public InternalMetric getResult(InternalMetric m) {
    return m;
  }

  @Override
  public InternalMetric merge(InternalMetric m1, InternalMetric m2) {
    return merge0(m1, m2);
  }
}
