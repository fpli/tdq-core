package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrdernessTimestampExtractorForLoad<T> extends
    BoundedOutOfOrdernessTimestampExtractor<T> {

  public SojBoundedOutOfOrdernessTimestampExtractorForLoad(
      Time maxOutOfOrderness) {
    super(maxOutOfOrderness);
  }

  @Override
  public long extractTimestamp(T t) {
    if (t instanceof SojSession) {
      SojSession sojSession = (SojSession) t;
      return sojSession.getStartTimestamp();
    } else if (t instanceof SojEvent) {
      SojEvent sojEvent = (SojEvent) t;
      return Long.parseLong(sojEvent.getEventTimestamp());
    } else {
      return 0;
    }
  }
}
