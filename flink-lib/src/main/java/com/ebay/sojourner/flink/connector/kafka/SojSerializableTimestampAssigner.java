package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.model.RawEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;

@Slf4j
public class SojSerializableTimestampAssigner<T> implements SerializableTimestampAssigner<T> {

  @Override
  public long extractTimestamp(T element, long recordTimestamp) {

    long field = System.currentTimeMillis();

    try {
//      field = TimestampFieldExtractor.getField(element);
      field = ((RawEvent) element).getEventTimestamp();
    } catch (Exception e) {
      log.warn("extract timestamp failed" + field);
    }

    return field;
  }
}
