package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.SojBytesEvent;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.configuration.Configuration;

public class SojBytesEventFilterFunction extends RichFilterFunction<SojBytesEvent> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public boolean filter(SojBytesEvent sojBytes) throws Exception {
    byte[] messagekey = sojBytes.getMessagekey();
    byte[] message = sojBytes.getMessage();
    return messagekey != null && message != null;
  }
}
