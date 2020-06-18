package com.ebay.sojourner.flink.connectors.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaConfig {
  private String topic;
  private String brokers;
  private String groupId;
}
