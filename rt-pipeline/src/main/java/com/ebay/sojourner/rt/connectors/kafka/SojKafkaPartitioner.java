package com.ebay.sojourner.rt.connectors.kafka;

import java.io.UnsupportedEncodingException;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;
import org.apache.flink.util.Preconditions;

public class SojKafkaPartitioner<T> extends FlinkKafkaPartitioner<T> {

  private static final String CHAR_SET = "utf-8";

  @Override
  public int partition(T record, byte[] key, byte[] value, String targetTopic,
      int[] partitions) {
    Preconditions.checkArgument(
        partitions != null && partitions.length > 0,
        "Partitions of the target topic is empty.");
    try {
      String keyStr = new String(key, CHAR_SET);
      return Math.abs(keyStr.hashCode() % partitions.length);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
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
