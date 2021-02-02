package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.util.SojTimestamp;
import org.apache.flink.api.common.functions.RichMapFunction;

public class SojEventTimestampTransMapFunction extends RichMapFunction<SojEvent, SojEvent> {

  @Override
  public SojEvent map(SojEvent value) throws Exception {

    if (value.getEventTimestamp() != null) {
      value.setEventTimestamp(
          SojTimestamp.getSojTimestamp(value.getEventTimestamp()));
    }
    return value;
  }
}
