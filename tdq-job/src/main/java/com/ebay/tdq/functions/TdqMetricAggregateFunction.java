package com.ebay.tdq.functions;

import com.ebay.tdq.rules.ExpressionRegistry;
import com.ebay.tdq.rules.TdqMetric;
import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @author juntzhang
 */
public class TdqMetricAggregateFunction implements AggregateFunction<TdqMetric, TdqMetric, TdqMetric> {
  public static TdqMetric merge0(TdqMetric m1, TdqMetric m2) {
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
            m1.getExprMap().getOrDefault(expr, 0d),
            m2.getExprMap().getOrDefault(expr, 0d)
        );
        m1.putExpr(expr, newV);
      });
      return m1;
    }
  }

  @Override
  public TdqMetric createAccumulator() {
    return null;
  }

  @Override
  public TdqMetric add(TdqMetric m1, TdqMetric m2) {
    return merge0(m1, m2);
  }

  @Override
  public TdqMetric getResult(TdqMetric m) {
    return m;
  }

  @Override
  public TdqMetric merge(TdqMetric m1, TdqMetric m2) {
    return merge0(m1, m2);
  }
}
