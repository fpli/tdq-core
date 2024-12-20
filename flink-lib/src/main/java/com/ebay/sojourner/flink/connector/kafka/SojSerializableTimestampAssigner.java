package com.ebay.sojourner.flink.connector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;

@Slf4j
public class SojSerializableTimestampAssigner<T> implements SerializableTimestampAssigner<T> {

  @Override
  public long extractTimestamp(T element, long recordTimestamp) {
    long t =  getEventTime(element);
    return t;
  }

  // due to local aggregate, we need treat TDQ watermark to 1 minute.
  public static <T> long getEventTime(T event) {
    long field = System.currentTimeMillis();
    try {
      field = TimestampFieldExtractor.getField(event);
    } catch (Exception e) {
      log.warn("extract timestamp failed" + field);
    }
    return (field / 60_000) * 60_000;
  }
}
