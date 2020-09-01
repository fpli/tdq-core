package com.ebay.sojourner.flink.common.window;

import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

public class OpenSessionFiringTrigger<W extends Window> extends Trigger<Object, W> {

  private static final long serialVersionUID = 1L;
  private final ReducingStateDescriptor<Long> lastTimestampStateDesc =
      new ReducingStateDescriptor<>("lastTimestamp", new maximum(), LongSerializer.INSTANCE);
  private Trigger countTrigger = CountTrigger.of(1);
  private Trigger eventTimeTrigger = EventTimeTrigger.create();
  private List<Trigger> triggers = new ArrayList<>();

  private OpenSessionFiringTrigger() {
    triggers.add(countTrigger);
    triggers.add(eventTimeTrigger);
  }

  public static <W extends Window> OpenSessionFiringTrigger<W> create() {
    return new OpenSessionFiringTrigger<>();
  }

  @Override
  public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx)
      throws Exception {
    if (ctx.getCurrentWatermark() >= window.maxTimestamp()) {
      return TriggerResult.CONTINUE;
    }

    List<TriggerResult> results = new ArrayList<>();
    for (Trigger trigger : triggers) {
      results.add(trigger.onElement(element, timestamp, window, ctx));
    }
    if (results.contains(TriggerResult.FIRE)) {
      return TriggerResult.FIRE;
    } else {
      ReducingState<Long> lastTiemstampState = ctx.getPartitionedState(lastTimestampStateDesc);
      String lastDateStr = SojTimestamp.getDateStrWithUnixTimestamp(
          lastTiemstampState.get() == null ? 0L : lastTiemstampState.get());
      String currentDateStr = SojTimestamp.getDateStrWithUnixTimestamp(timestamp);
      if (lastTiemstampState.get() != null && lastTiemstampState.get() < timestamp
          && !currentDateStr.equals(lastDateStr)) {
        lastTiemstampState.clear();
        lastTiemstampState.add(timestamp);
        return TriggerResult.FIRE;
      }
      return TriggerResult.CONTINUE;
    }
  }

  @Override
  public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
    List<TriggerResult> results = new ArrayList<>();
    for (Trigger trigger : triggers) {
      results.add(trigger.onEventTime(time, window, ctx));
    }
    if (results.contains(TriggerResult.FIRE)) {
      return TriggerResult.FIRE;
    } else {
      ReducingState<Long> lastTiemstampState = ctx.getPartitionedState(lastTimestampStateDesc);
      String lastDateStr = SojTimestamp.getDateStrWithUnixTimestamp(
          lastTiemstampState.get() == null ? 0L : lastTiemstampState.get());
      String currentDateStr = SojTimestamp.getDateStrWithUnixTimestamp(time);
      if (lastTiemstampState.get() != null && lastTiemstampState.get() < time && !currentDateStr
          .equals(lastDateStr)) {
        lastTiemstampState.clear();
        lastTiemstampState.add(time);
        return TriggerResult.FIRE;
      }
      return TriggerResult.CONTINUE;
    }
  }

  @Override
  public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
    List<TriggerResult> results = new ArrayList<>();
    for (Trigger trigger : triggers) {
      results.add(trigger.onProcessingTime(time, window, ctx));
    }
    if (results.contains(TriggerResult.FIRE)) {
      return TriggerResult.FIRE;
    } else {
      return TriggerResult.CONTINUE;
    }
  }

  @Override
  public void clear(W window, TriggerContext ctx) throws Exception {
    for (Trigger trigger : triggers) {
      trigger.clear(window, ctx);
    }
    ctx.getPartitionedState(lastTimestampStateDesc).clear();
  }

  @Override
  public boolean canMerge() {
    return true;
  }

  @Override
  public void onMerge(W window, OnMergeContext ctx) throws Exception {
    for (Trigger trigger : triggers) {
      trigger.onMerge(window, ctx);
    }
    ctx.mergePartitionedState(lastTimestampStateDesc);
  }

  @Override
  public String toString() {
    return "SojTrigger()";
  }

  private static class maximum implements ReduceFunction<Long> {

    private static final long serialVersionUID = 1L;

    @Override
    public Long reduce(Long value1, Long value2) throws Exception {
      return Math.max(value1, value2);
    }

  }
}
