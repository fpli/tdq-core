package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrdernessTimestampExtractor extends
    BoundedOutOfOrdernessTimestampExtractor<RawEvent> {

  public SojBoundedOutOfOrdernessTimestampExtractor(
      Time maxOutOfOrderness) {
    super(maxOutOfOrderness);
  }

  @Override
  public long extractTimestamp(RawEvent rawEvent) {
    return rawEvent.getRheosHeader().getEventCreateTimestamp();
  }
}
