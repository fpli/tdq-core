package com.ebay.sojourner.ubd.rt.util;

import java.util.List;
import lombok.Data;

@Data
public class KafkaProducerConfig {

  private List<String> bootstrapServersForSession;
  private List<String> bootstrapServersForEvent;
  private List<String> bootstrapServersForBot;
  private List<String> bootstrapServersForCopy;
  private List<String> bootstrapServersForCrossSessionDQ;
  private String sessionTopic;
  private String eventTopic;
  private String botTopic;
  private String copyTopic;
  private String crossSessionDQTopic;
}
