package com.ebay.tdq.functions;

import com.ebay.tdq.rules.TdqMetric;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetricProcessWindowTagFunction
    extends ProcessWindowFunction<TdqMetric, TdqMetric, String, TimeWindow> {
  public final Map<Long, OutputTag<TdqMetric>> tagMap;

  public TdqMetricProcessWindowTagFunction(Map<Long, OutputTag<TdqMetric>> tagMap) {
    this.tagMap = tagMap;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public void process(String tag, Context context,
      Iterable<TdqMetric> iterable, Collector<TdqMetric> collector) {
    collect(iterable.iterator().next(), context);
  }

  void collect(TdqMetric m, Context context) {
    m.setEventTime(context.window().getEnd());
    OutputTag<TdqMetric> outputTag = tagMap.get(m.getWindow());
    if (outputTag != null) {
      context.output(outputTag, m);
    } else {
      log.warn("Drop tdq metric{}, windows not supported!", m);
    }
  }
}
