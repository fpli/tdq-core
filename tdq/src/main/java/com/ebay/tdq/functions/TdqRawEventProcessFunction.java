package com.ebay.tdq.functions;

import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.TimestampFieldExtractor;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.metrics.Meter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqRawEventProcessFunction
    extends BroadcastProcessFunction<RawEvent, PhysicalPlan, TdqMetric> {
  private final MapStateDescriptor<String, PhysicalPlan> stateDescriptor;
  private final int cacheCapacity;
  private Counter tdqErrorEventsCount;
  private Counter tdqDroppedEventsCount;
  private Meter tdqProcessEventsMeter;
  private Meter tdqProcessElementMeter;
  private Meter tdqCollectMeter;
  private Histogram tdqProcessMetricHistogram;
  private Histogram tdqProcessElementHistogram;
  public long logCurrentTimeMillis = 0L;
  public Map<String, TdqMetric> cache;
  public long cacheCurrentTimeMillis = System.currentTimeMillis();
  public long earliestEventTimeMillis = 0L;


  public TdqRawEventProcessFunction(MapStateDescriptor<String, PhysicalPlan> descriptor,
      int cacheCapacity) {
    this.stateDescriptor = descriptor;
    this.cacheCapacity   = cacheCapacity;
    this.cache           = new HashMap<>(cacheCapacity);
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


  private boolean needFlush() {
    return cache.size() >= cacheCapacity || (System.currentTimeMillis() - cacheCurrentTimeMillis) > 1000;
  }

  // cache event time should be in whole minute, like 1:01:00 ~ 1:01:59
  private boolean crossWindow(Long currentEventTime) {
    return (earliestEventTimeMillis / 1000 / 60) != (currentEventTime / 1000 / 60);
  }

  private void collect(PhysicalPlan plan, TdqMetric curr, Collector<TdqMetric> collector) {
    if (earliestEventTimeMillis == 0L || earliestEventTimeMillis > curr.getEventTime()) {
      earliestEventTimeMillis = curr.getEventTime();
    }

    if (crossWindow(curr.getEventTime())) {
      // flush cache
      for (TdqMetric m : cache.values()) {
        collector.collect(m);
      }
      tdqCollectMeter.markEvent();
      cache.clear();
      earliestEventTimeMillis = 0L;
    }

    TdqMetric last = cache.get(curr.getUid());
    if (last != null) {
      curr = plan.merge(last, curr);
    }
    cache.put(curr.getUid(), curr);

    if (needFlush()) {
      // flush all
      for (TdqMetric m : cache.values()) {
        collector.collect(m);
      }
      tdqCollectMeter.markEvent();
      cache.clear();
      cacheCurrentTimeMillis  = System.currentTimeMillis();
      earliestEventTimeMillis = 0L;
    }
  }

  @Override
  public void processElement(RawEvent event, ReadOnlyContext ctx,
      Collector<TdqMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    ReadOnlyBroadcastState<String, PhysicalPlan> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    boolean success = false;
    Long ts = TimestampFieldExtractor.getField(event);
    for (Map.Entry<String, PhysicalPlan> entry : broadcastState.immutableEntries()) {
      long s = System.nanoTime();
      TdqMetric metric = process(event, entry.getValue());
      if (metric != null) {
        metric.setEventTime(ts);
        collect(entry.getValue(), metric, collector);
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


  private TdqMetric process(RawEvent event, PhysicalPlan plan) {
    try {
      return plan.process(event);
    } catch (Exception e) {
      tdqErrorEventsCount.inc();
      if ((System.currentTimeMillis() - logCurrentTimeMillis) > 30000) {
        log.warn(e.getMessage(), e);
        log.warn("Drop event={},plan={}", event, plan);
        logCurrentTimeMillis = System.currentTimeMillis();
      }
      return null;
    }
  }


  @Override
  public void processBroadcastElement(PhysicalPlan plan,
      Context ctx, Collector<TdqMetric> collector) throws Exception {
    BroadcastState<String, PhysicalPlan> broadcastState = ctx.getBroadcastState(stateDescriptor);
    broadcastState.put(plan.uuid(), plan);
  }
}
