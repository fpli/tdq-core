package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojEventBoundedOutOfOrdernessTimestampExtractor extends
    BoundedOutOfOrdernessTimestampExtractor<SojEvent> {

  public SojEventBoundedOutOfOrdernessTimestampExtractor(
      Time maxOutOfOrderness) {
    super(maxOutOfOrderness);
  }

  @Override
  public long extractTimestamp(SojEvent sojEvent) {
    return Long.parseLong(sojEvent.getEventTimestamp());
  }
}
