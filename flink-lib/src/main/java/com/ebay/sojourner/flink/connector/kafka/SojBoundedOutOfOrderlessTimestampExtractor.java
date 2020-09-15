package com.ebay.sojourner.flink.connector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

@Slf4j
public class SojBoundedOutOfOrderlessTimestampExtractor<T> extends
    BoundedOutOfOrdernessTimestampExtractor<T> {

  private static final long serialVersionUID = -1902049986991898214L;

  public SojBoundedOutOfOrderlessTimestampExtractor(
      Time maxOutOfOrderless) {
    super(maxOutOfOrderless);
  }

  @Override
  public long extractTimestamp(T t) {

    long field = System.currentTimeMillis();

    try {
       field = TimestampFieldExtractor.getField(t);
    } catch (Exception e) {
      log.warn("extract timestamp failed" + field);
    }

    return field;
  }
}
