package com.ebay.sojourner.flink.common.window;

import com.ebay.sojourner.common.util.SojTimestamp;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

@Slf4j
public class PassnightTrigger
    extends Trigger<Object, TimeWindow> {

  private static final long serialVersionUID = 1L;
  private final ReducingStateDescriptor<Long> minTimestampStateDesc =
      new ReducingStateDescriptor<>("minmunTimestamp", new minum(), LongSerializer.INSTANCE);

  private PassnightTrigger() {
  }

  public static PassnightTrigger create() {
    return new PassnightTrigger();
  }

  @Override
  public TriggerResult onElement(Object element, long timestamp, TimeWindow window,
      TriggerContext ctx) throws Exception {

    ReducingState<Long> minTiemstampState = ctx.getPartitionedState(minTimestampStateDesc);
    long timerMills = SojTimestamp.castUnixTimestampToDateMINS1(timestamp);
    minTiemstampState.add(timerMills);
    ctx.registerEventTimeTimer(minTiemstampState.get());
    return TriggerResult.CONTINUE;

  }

  @Override
  public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
    return TriggerResult.FIRE;
  }

  @Override
  public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx)
      throws Exception {
    return TriggerResult.CONTINUE;
  }

  @Override
  public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
    ReducingState<Long> minTiemstampState = ctx.getPartitionedState(minTimestampStateDesc);
    if(minTiemstampState.get()!=null) {
      ctx.deleteEventTimeTimer(minTiemstampState.get());
    }
    ctx.getPartitionedState(minTimestampStateDesc).clear();
  }

  @Override
  public boolean canMerge() {
    return true;
  }

  @Override
  public void onMerge(TimeWindow window,
      OnMergeContext ctx) {
    ctx.mergePartitionedState(minTimestampStateDesc);
    try {
      ReducingState<Long> minTiemstampState = ctx.getPartitionedState(minTimestampStateDesc);
      ctx.registerEventTimeTimer(minTiemstampState.get());
    } catch (Exception e) {
      log.error("Passnight onMerge issue", e);
    }
  }

  @Override
  public String toString() {
    return "PassnightTrigger()";
  }

  private static class minum implements ReduceFunction<Long> {

    private static final long serialVersionUID = 1L;

    @Override
    public Long reduce(Long value1, Long value2) throws Exception {
      return Math.min(value1, value2);
    }

  }
}