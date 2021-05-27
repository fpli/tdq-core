package com.ebay.tdq.functions;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.DateUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetric2ndAggrProcessWindowFunction
    extends ProcessWindowFunction<TdqMetric, TdqMetric, String, TimeWindow> {
  private transient Map<String, Counter> counterMap;
  private transient MetricGroup group;

  @Override
  public void open(Configuration parameters) throws Exception {
    counterMap = new HashMap<>();
    group      = this.getRuntimeContext().getMetricGroup().addGroup("tdq2");
    super.open(parameters);
  }

  public void inc(String key, long v) {
    Counter counter = counterMap.get(key);
    if (counter == null) {
      counter = group.counter(key);
      counterMap.put(key, counter);
    }
    counter.inc(v);
  }

  @Override
  public void process(String s, Context context, Iterable<TdqMetric> elements,
      Collector<TdqMetric> out) {
    elements.forEach(metric -> {
      if (metric.getExprMap() != null && metric.getExprMap().get("p1") != null) {
        inc(DateUtils.getMinBuckets(metric.getEventTime(), 5) + "_" + metric.getMetricKey(),
            (long) (double) metric.getExprMap().get("p1"));
      }
      metric.setEventTime(context.window().getEnd() - 1);
      out.collect(metric);
    });
  }
}
