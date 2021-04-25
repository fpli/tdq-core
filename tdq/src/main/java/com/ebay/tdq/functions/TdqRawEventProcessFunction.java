package com.ebay.tdq.functions;

import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.TimestampFieldExtractor;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Counter;
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
  private final int partitions;
  private final Random random = new Random();
  private Counter tdqDroppedEventsCount;
  private Meter meter;
  private Histogram tdqProcessMetricHistogram;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    meter = getRuntimeContext().getMetricGroup().meter("tdqDroppedEventsCount",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqDroppedEventsCount = getRuntimeContext().getMetricGroup().counter("tdqDroppedEventsCount");
    tdqProcessMetricHistogram = getRuntimeContext().getMetricGroup().histogram("tdqProcessMetricHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
  }

  public TdqRawEventProcessFunction(MapStateDescriptor<String, PhysicalPlan> descriptor,
      int partitions) {
    this.stateDescriptor = descriptor;
    this.partitions      = partitions;
  }

  public TdqRawEventProcessFunction(MapStateDescriptor<String, PhysicalPlan> descriptor) {
    this(descriptor, -1);
  }

  @Override
  public void processElement(RawEvent event, ReadOnlyContext ctx,
      Collector<TdqMetric> collector) throws Exception {
    ReadOnlyBroadcastState<String, PhysicalPlan> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    boolean success = false;
    Long ts = TimestampFieldExtractor.getField(event);
    for (Map.Entry<String, PhysicalPlan> entry : broadcastState.immutableEntries()) {
      long s = System.currentTimeMillis();
      TdqMetric metric = entry.getValue().process(event);
      if (metric != null) {
        if (partitions != -1) {
          metric.setPartition(getInt() % partitions);
        }
        metric.setEventTime(ts);
        collector.collect(metric);
        //        log.warn("processElement=>{}", metric);
        tdqProcessMetricHistogram.update(System.currentTimeMillis() - s);
      }
      meter.markEvent();
      success = true;
    }
    if (!success) {
      // todo how to deal with event happened after broadcast
      // if (tdqDroppedEventsCount.getCount() % 100 == 0) {
      //   log.warn("Drop events {}", event);
      // }
      tdqDroppedEventsCount.inc();
    }
  }

  public int getInt() {
    return Math.abs(random.nextInt());
  }


  @Override
  public void processBroadcastElement(PhysicalPlan plan,
      Context ctx, Collector<TdqMetric> collector) throws Exception {
    BroadcastState<String, PhysicalPlan> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    log.warn("{}", plan);
    broadcastState.put(plan.uuid(), plan);
  }
}
