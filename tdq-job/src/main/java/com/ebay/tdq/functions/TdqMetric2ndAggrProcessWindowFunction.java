package com.ebay.tdq.functions;

import com.ebay.tdq.common.model.TdqMetric;
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
    group = this.getRuntimeContext().getMetricGroup().addGroup("tdq2");
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
      metric.setEventTime(context.window().getEnd() - 1);
      out.collect(metric);
    });
  }
}
