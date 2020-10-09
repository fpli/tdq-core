package com.ebay.sojourner.flink.window;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

public class CompositeTrigger<W extends Window> extends Trigger<Object, W> {

  private static final long serialVersionUID = 1L;

  private List<Trigger> triggers = new ArrayList<>();

  private CompositeTrigger(List<Trigger> triggers) {
    this.triggers = triggers;
  }

  @Override
  public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx)
      throws Exception {
    List<TriggerResult> results = new ArrayList<>();
    for (Trigger trigger : triggers) {
      results.add(trigger.onElement(element, timestamp, window, ctx));
    }
    if (results.contains(TriggerResult.FIRE)) {
      return TriggerResult.FIRE;
    } else {
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
  public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
    List<TriggerResult> results = new ArrayList<>();
    for (Trigger trigger : triggers) {
      results.add(trigger.onEventTime(time, window, ctx));
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
  }

  @Override
  public boolean canMerge() {
    boolean canMerge = true;
    for (Trigger trigger : triggers) {
      canMerge = canMerge && trigger.canMerge();
    }
    return canMerge;
  }

  @Override
  public void onMerge(W window, OnMergeContext ctx) throws Exception {
    for (Trigger trigger : triggers) {
      trigger.onMerge(window, ctx);
    }
  }

  public static class Builder {

    private List<Trigger> triggers = new ArrayList<>();

    public static Builder create() {
      return new Builder();
    }

    public Builder trigger(Trigger trigger) {
      triggers.add(trigger);
      return this;
    }

    public CompositeTrigger build() {
      return new CompositeTrigger(triggers);
    }
  }
}
