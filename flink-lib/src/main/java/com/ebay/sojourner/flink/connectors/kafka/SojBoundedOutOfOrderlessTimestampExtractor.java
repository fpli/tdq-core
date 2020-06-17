package com.ebay.sojourner.flink.connectors.kafka;

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrderlessTimestampExtractor<T> extends
    BoundedOutOfOrdernessTimestampExtractor<T> {

  private static final long serialVersionUID = -1902049986991898214L;

  public SojBoundedOutOfOrderlessTimestampExtractor(
      Time maxOutOfOrderless) {
    super(maxOutOfOrderless);
  }

  @Override
  public long extractTimestamp(T t) {

    long filed = TimestampFiledExtractManager.getField(t);
    return filed;
  }
}
