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
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetric1stAggrProcessWindowFunction
    extends ProcessWindowFunction<TdqMetric, TdqMetric, String, TimeWindow> {

  public final Map<Long, OutputTag<TdqMetric>> tagMap;
  private transient Map<String, Counter> counterMap;
  private transient MetricGroup group;

  public TdqMetric1stAggrProcessWindowFunction(Map<Long, OutputTag<TdqMetric>> tagMap) {
    this.tagMap = tagMap;
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
  public void open(Configuration parameters) throws Exception {
    counterMap = new HashMap<>();
    group = this.getRuntimeContext().getMetricGroup().addGroup("tdq");
    super.open(parameters);
  }

  @Override
  public void process(String tag, Context context,
      Iterable<TdqMetric> iterable, Collector<TdqMetric> collector) {
    iterable.forEach(tdqMetric -> collect(tdqMetric, context));
  }

  void collect(TdqMetric m, Context context) {
    OutputTag<TdqMetric> outputTag = tagMap.get(m.getWindow());
    if (outputTag != null) {
      context.output(outputTag, m);
    } else {
      inc("dropMetric", 1);
      log.warn("Drop tdq metric {}, windows not supported!", m);
    }
  }
}
