package com.ebay.tdq.utils;

import com.codahale.metrics.SlidingWindowReservoir;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.metrics.Meter;
import org.apache.flink.metrics.MetricGroup;

/**
 * @author juntzhang
 */
public class TdqMetricGroup {
  private MetricGroup group;
  private Meter tdqProcessEventsMeter;
  private Meter tdqProcessElementMeter;
  private Histogram tdqProcessMetricHistogram;
  private Histogram tdqProcessElementHistogram;
  private Map<String, Counter> counterMap;

  protected TdqMetricGroup() {
  }

  public TdqMetricGroup(MetricGroup group) {
    this.group                 = group.addGroup("tdq");
    tdqProcessEventsMeter      = group.meter("ProcessEventsMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqProcessElementMeter     = group.meter("ProcessElementMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqProcessMetricHistogram  = group.histogram("ProcessMetricHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    tdqProcessElementHistogram = group.histogram("ProcessElementHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    counterMap                 = new HashMap<>();
  }

  public void gauge(LocalCache cache) {
    group.gauge("CacheSize", (Gauge<Integer>) cache::size);
  }

  public void markEvent() {
    tdqProcessEventsMeter.markEvent();
  }

  public void markElement(long s) {
    tdqProcessElementMeter.markEvent();
    tdqProcessElementHistogram.update(System.nanoTime() - s);
  }

  public void updateEventHistogram(long s) {
    tdqProcessMetricHistogram.update(System.nanoTime() - s);
  }

  public void inc(String key) {
    Counter counter = counterMap.get(key);
    if (counter == null) {
      counter = group.counter(key);
      counterMap.put(key, counter);
    }
    counter.inc();
  }

  public void inc(String key, long v) {
    Counter counter = counterMap.get(key);
    if (counter == null) {
      counter = group.counter(key);
      counterMap.put(key, counter);
    }
    counter.inc(v);
  }
}
