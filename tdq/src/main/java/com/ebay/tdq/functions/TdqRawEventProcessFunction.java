package com.ebay.tdq.functions;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
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
    for (Map.Entry<String, PhysicalPlan> entry : broadcastState.immutableEntries()) {
      TdqMetric metric = entry.getValue().process(event);
      if (metric != null) {
        if (partitions != -1) {
          metric.setPartition(getInt() % partitions);
        }
        collector.collect(metric);
      }
      success = true;
    }
    if (!success) {
      // todo how to deal with event happened after broadcast
      log.warn("Drop events {}", event);
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
    broadcastState.put(plan.uuid(), plan);
  }
}
