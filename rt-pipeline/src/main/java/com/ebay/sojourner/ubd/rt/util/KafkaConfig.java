package com.ebay.sojourner.ubd.rt.util;

import java.util.List;
import lombok.Data;

@Data
public class KafkaConfig {
  private List<String> bootstrapServers;
  private String groupId;
  private String topic;
}
