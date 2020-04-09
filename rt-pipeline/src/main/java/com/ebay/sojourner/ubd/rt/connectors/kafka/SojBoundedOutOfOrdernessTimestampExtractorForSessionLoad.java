package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojSession;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrdernessTimestampExtractorForSessionLoad extends
    BoundedOutOfOrdernessTimestampExtractor<SojSession> {

  public SojBoundedOutOfOrdernessTimestampExtractorForSessionLoad(
      Time maxOutOfOrderness) {
    super(maxOutOfOrderness);
  }

  @Override
  public long extractTimestamp(SojSession sojSession) {
    return sojSession.getAbsStartTimestamp();
  }
}
