package com.ebay.sojourner.rt.operators.event;

import com.ebay.sojourner.common.model.SojEvent;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class SojEventFilterFunction extends RichFilterFunction<SojEvent> {

  @Override
  public boolean filter(SojEvent value) throws Exception {
    if (value.getGuid() != null) {
      return value.getGuid().hashCode() % 20 == 0;
    }

    return false;
  }
}
