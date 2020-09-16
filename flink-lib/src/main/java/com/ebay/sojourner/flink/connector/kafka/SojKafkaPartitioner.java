package com.ebay.sojourner.flink.connector.kafka;

import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;
import org.apache.flink.util.Preconditions;

@Slf4j
public class SojKafkaPartitioner<T> extends FlinkKafkaPartitioner<T> {

  private static final String CHAR_SET = "utf-8";

  @Override
  public int partition(T record, byte[] key, byte[] value, String targetTopic,
      int[] partitions) {

    Preconditions.checkNotNull(
        partitions, "partition of the target topic is null.");

    Preconditions.checkArgument(partitions.length > 0,
        "partition of the target topic is empty.");

    try {
      String keyStr = new String(key, CHAR_SET);
      return Math.abs(keyStr.hashCode() % partitions.length);
    } catch (UnsupportedEncodingException e) {
      log.error("get partition number failed", e);
    }
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof SojKafkaPartitioner;
  }

  @Override
  public int hashCode() {
    return SojKafkaPartitioner.class.hashCode();
  }
}
