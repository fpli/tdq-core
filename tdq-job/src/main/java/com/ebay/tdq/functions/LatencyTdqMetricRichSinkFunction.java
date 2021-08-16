package com.ebay.tdq.functions;

import com.ebay.tdq.common.model.InternalMetric;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @author juntzhang
 */
public class LatencyTdqMetricRichSinkFunction extends RichSinkFunction<InternalMetric> {

  private transient MetricGroup group;
  private transient Map<String, Counter> counterMap;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    counterMap = new HashMap<>();
    group = this.getRuntimeContext().getMetricGroup().addGroup("tdq");
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
  public void invoke(InternalMetric metric, SinkFunction.Context context) {
    inc("tag_latency", 1);
  }
}
