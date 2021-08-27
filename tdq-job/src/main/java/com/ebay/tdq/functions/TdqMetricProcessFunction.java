package com.ebay.tdq.functions;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.planner.LkpManager;
import com.ebay.tdq.utils.TdqConfigManager;
import com.ebay.tdq.utils.TdqContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetricProcessFunction extends ProcessFunction<TdqMetric, InternalMetric> {

  private final TdqEnv tdqEnv;
  private final Random random = new Random();
  private transient MetricGroup metricGroup;
  private Map<String, Counter> counterMap;

  public TdqMetricProcessFunction(TdqContext tdqCxt) {
    this.tdqEnv = tdqCxt.getTdqEnv();
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    metricGroup = getRuntimeContext().getMetricGroup().addGroup("tdq");
    counterMap = new HashMap<>();
    TdqConfigManager.getInstance(tdqEnv).start();
    LkpManager.getInstance(tdqEnv).start();
  }

  @Override
  public void processElement(TdqMetric in, Context ctx, Collector<InternalMetric> out) {
    InternalMetric metric = new InternalMetric(in.getMetricName(), in.getEventTime());
    metric.setPartition(Math.abs(random.nextInt()) % tdqEnv.getOutputPartitions());
    Map<String, String> aggrExpressMap = TdqConfigManager.getInstance(tdqEnv).getMetricNameAggrExpressMap()
        .get(in.getMetricName());
    if (MapUtils.isNotEmpty(aggrExpressMap)) {
      aggrExpressMap.forEach(metric::putAggrExpress);
      in.getTags().forEach(metric::putTag);
      in.getValues().forEach(metric::putExpr);
      out.collect(metric.genMetricId());
    } else {
      inc("aggrExpressMapMiss");
    }
  }

  public void inc(String key) {
    Counter counter = counterMap.get(key);
    if (counter == null) {
      counter = metricGroup.counter(key);
      counterMap.put(key, counter);
    }
    counter.inc();
  }
}
