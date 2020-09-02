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
  private final ReducingStateDescriptor<Long> lastTimestampStateDesc =
      new ReducingStateDescriptor<>("lastTimestamp", new maximum(), LongSerializer.INSTANCE);

  private PassnightTrigger() {
  }

  public static PassnightTrigger create() {
    return new PassnightTrigger();
  }

  @Override
  public TriggerResult onElement(Object element, long timestamp, TimeWindow window,
      TriggerContext ctx) throws Exception {

    ReducingState<Long> lastTiemstampState = ctx.getPartitionedState(lastTimestampStateDesc);
    if (lastTiemstampState.get() == null) {
      lastTiemstampState.add(timestamp);
      long timerMills = SojTimestamp.castUnixTimestampToDateMINS1(timestamp);
      ctx.registerEventTimeTimer(timerMills);
      return TriggerResult.CONTINUE;
    } else {
      String lastDateStr = SojTimestamp.getDateStrWithUnixTimestamp(
          lastTiemstampState.get());
      String currentDateStr = SojTimestamp.getDateStrWithUnixTimestamp(timestamp);
      if (lastTiemstampState.get() < timestamp
          && !currentDateStr.equals(lastDateStr)) {
        lastTiemstampState.clear();
        long timerMills = SojTimestamp.castUnixTimestampToDateMINS1(timestamp);
        lastTiemstampState.add(timerMills);
        ctx.registerEventTimeTimer(timerMills);
        return TriggerResult.FIRE;
      } else {
        return TriggerResult.CONTINUE;
      }
    }

  }

  @Override
  public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
    try {
      ReducingState<Long> lastTiemstampState = ctx.getPartitionedState(lastTimestampStateDesc);
      lastTiemstampState.clear();
      long timerMills = SojTimestamp.castUnixTimestampToDateMINS1(ctx.getCurrentWatermark());
      lastTiemstampState.add(timerMills);
    } catch (Exception e) {
      log.error("PassnightTrigger onEventTime error", e);
    }
    return TriggerResult.FIRE;
  }

  @Override
  public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx)
      throws Exception {
    return TriggerResult.CONTINUE;
  }

  @Override
  public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
    ctx.deleteEventTimeTimer(window.maxTimestamp());
    ctx.getPartitionedState(lastTimestampStateDesc).clear();
  }

  @Override
  public boolean canMerge() {
    return true;
  }

  @Override
  public void onMerge(TimeWindow window,
      OnMergeContext ctx) {
    ReducingState<Long> lastTiemstampState = ctx.getPartitionedState(lastTimestampStateDesc);
    try {
      if (lastTiemstampState.get() != null) {
        long timerMills = lastTiemstampState.get();
        long currentWaterMark = ctx.getCurrentWatermark();
        if (currentWaterMark > timerMills) {
          lastTiemstampState.clear();
          timerMills = SojTimestamp.castUnixTimestampToDateMINS1(currentWaterMark);
          lastTiemstampState.add(timerMills);
        }
      }
    } catch (Exception e) {
      log.error("PassnightTrigger onMerge issue:", e);
    }
    ctx.mergePartitionedState(lastTimestampStateDesc);
  }

  @Override
  public String toString() {
    return "PassnightTrigger()";
  }

  private static class maximum implements ReduceFunction<Long> {

    private static final long serialVersionUID = 1L;

    @Override
    public Long reduce(Long value1, Long value2) throws Exception {
      return Math.max(value1, value2);
    }

  }
}