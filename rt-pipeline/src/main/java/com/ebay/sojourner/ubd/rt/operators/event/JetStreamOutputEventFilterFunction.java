package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class JetStreamOutputEventFilterFunction extends RichFilterFunction<JetStreamOutputEvent> {

  @Override
  public boolean filter(JetStreamOutputEvent jetStreamOutputEvent) throws Exception {

    return Math.abs(jetStreamOutputEvent.getGuid().hashCode() % 20) == 0;
  }
}
