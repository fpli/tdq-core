package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrdernessTimestampExtractorForEventLoad extends
    BoundedOutOfOrdernessTimestampExtractor<SojEvent> {

  public SojBoundedOutOfOrdernessTimestampExtractorForEventLoad(
      Time maxOutOfOrderness) {
    super(maxOutOfOrderness);
  }

  @Override
  public long extractTimestamp(SojEvent sojEvent) {
    return sojEvent.getIngestTime();
  }

}
