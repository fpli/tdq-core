package com.ebay.sojourner.flink.connector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;

@Slf4j
public class SojSerializableTimestampAssigner<T> implements SerializableTimestampAssigner<T> {

  @Override
  public long extractTimestamp(T element, long recordTimestamp) {

    long field = System.currentTimeMillis();

    try {
      field = TimestampFieldExtractor.getField(element);
    } catch (Exception e) {
      log.warn("extract timestamp failed" + field);
    }/* finally {
      if (field > System.currentTimeMillis()) {
        log.error("Watermark error field=" + field + ", larger than current time!");
        field = System.currentTimeMillis();
      }
    }*/

    return field;
  }
}
