package com.ebay.sojourner.ubd.rt.common.windows;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

public class SojTrigger<W extends Window> extends Trigger<Object, W> {

  private static final long serialVersionUID = 1L;

  private Trigger countTrigger = CountTrigger.of(1);
  private Trigger eventTimeTrigger = EventTimeTrigger.create();
  private List<Trigger> triggers = new ArrayList<>();

  private SojTrigger() {
    triggers.add(countTrigger);
    triggers.add(eventTimeTrigger);
  }

  public static <W extends Window> SojTrigger<W> create() {
    return new SojTrigger<>();
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
  }

  @Override
  public String toString() {
    return "SojTrigger()";
  }
}
