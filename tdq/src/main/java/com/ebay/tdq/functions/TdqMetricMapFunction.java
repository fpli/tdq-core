package com.ebay.tdq.functions;

import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.TimestampFieldExtractor;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.metrics.Meter;
import org.apache.flink.util.Collector;

import static com.ebay.tdq.functions.TdqConfigSourceFunction.getPhysicalPlanMap;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetricMapFunction
    extends RichFlatMapFunction<RawEvent, TdqMetric> {
  private static final Map<String, PhysicalPlan> cfgMap;
  static {
    try {
      cfgMap = getPhysicalPlanMap();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  private final int partitions;
  private final int cacheCapacity;

  private final Random random = new Random();
  private Counter tdqErrorEventsCount;
  private Counter tdqDroppedEventsCount;
  private Meter tdqProcessEventsMeter;
  private Meter tdqProcessElementMeter;
  private Meter tdqCollectMeter;
  private Histogram tdqProcessMetricHistogram;
  private Histogram tdqProcessElementHistogram;

  public TdqMetricMapFunction(int partitions, int cacheCapacity) {
    this.partitions    = partitions;
    this.cacheCapacity = cacheCapacity;
    this.cache         = new HashMap<>(cacheCapacity);
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    tdqProcessEventsMeter      = getRuntimeContext().getMetricGroup().meter("tdqProcessEventsMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqCollectMeter            = getRuntimeContext().getMetricGroup().meter("tdqCollectMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqProcessElementMeter     = getRuntimeContext().getMetricGroup().meter("tdqProcessElementMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqDroppedEventsCount      = getRuntimeContext().getMetricGroup().counter("tdqDroppedEventsCount");
    tdqErrorEventsCount        = getRuntimeContext().getMetricGroup().counter("tdqErrorEventsCount");
    tdqProcessMetricHistogram  = getRuntimeContext().getMetricGroup().histogram("tdqProcessMetricHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    tdqProcessElementHistogram = getRuntimeContext().getMetricGroup().histogram("tdqProcessElementHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    getRuntimeContext().getMetricGroup().gauge("cacheSize", (Gauge<Integer>) () -> cache.size());
  }

  private TdqMetric process(RawEvent event, PhysicalPlan plan) {
    try {
      return plan.process(event);
    } catch (Exception e) {
      tdqErrorEventsCount.inc();
      if (tdqDroppedEventsCount.getCount() % 1000 == 0) {
        log.warn(e.getMessage(), e);
      }
      return null;
    }
  }

  public static Integer getInt() {
    return (int) (Math.abs(UUID.randomUUID().hashCode() + System.nanoTime()) % 1000000);
  }

  public Map<String, TdqMetric> cache;
  public long cacheCurrentTimeMillis = System.currentTimeMillis();
  public long logCurrentTimeMillis = 0L;

  public boolean needCollect() {
    return cache.size() >= cacheCapacity || (System.currentTimeMillis() - cacheCurrentTimeMillis) > 1000;
  }

  public void collect(TdqMetric curr, Collector<TdqMetric> collector) {
    TdqMetric last = cache.get(curr.getMetricKey());
    if (last != null) {
      curr = cfgMap.get(curr.getMetricKey()).merge(last, curr);
    }
    cache.put(curr.getMetricKey(), curr);

    if (needCollect()) {
      for (TdqMetric m : cache.values()) {
        collector.collect(m);
      }
      tdqCollectMeter.markEvent();
      cache.clear();
      cacheCurrentTimeMillis = System.currentTimeMillis();
    }
  }

  @Override
  public void flatMap(RawEvent event, Collector<TdqMetric> collector) {
    long s1 = System.nanoTime();
    boolean success = false;
    Long ts = TimestampFieldExtractor.getField(event);
    for (Map.Entry<String, PhysicalPlan> entry : cfgMap.entrySet()) {
      long s = System.nanoTime();
      TdqMetric metric = process(event, entry.getValue());
      if (metric != null) {
        if (partitions != -1) {
          metric.setPartition(getInt() % partitions);
        }
        metric.setEventTime(ts);
        collect(metric, collector);
        tdqProcessMetricHistogram.update(System.nanoTime() - s);
      }
      tdqProcessEventsMeter.markEvent();
      success = true;
    }
    if (!success) {
      if ((System.currentTimeMillis() - logCurrentTimeMillis) > 30000) {
        log.warn("Drop events {}", event);
        logCurrentTimeMillis = System.currentTimeMillis();
      }
      tdqDroppedEventsCount.inc();
    }
    tdqProcessElementMeter.markEvent();
    tdqProcessElementHistogram.update(System.nanoTime() - s1);
  }
}
