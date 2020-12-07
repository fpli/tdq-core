package com.ebay.sojourner.flink.connector.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlinkKafkaSourceConfigWrapper {
  private KafkaConsumerConfig kafkaConsumerConfig;
  private int outOfOrderlessInMin;
  private long fromTimestamp;
}
