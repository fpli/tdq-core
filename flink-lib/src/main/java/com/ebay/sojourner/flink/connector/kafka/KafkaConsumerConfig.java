package com.ebay.sojourner.flink.connector.kafka;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaConsumerConfig {

  private List<String> topicList;
  private String brokers;
  private String groupId;
}
