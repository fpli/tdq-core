package com.ebay.sojourner.ubd.rt.util;

import java.util.List;
import lombok.Data;

@Data
public class KafkaConsumerConfig {

  private List<String> bootstrapServersForRNO;
  private List<String> bootstrapServersForSLC;
  private List<String> bootstrapServersForLVS;
  private List<String> bootstrapServersForQA;
  private List<String> bootstrapServersForJetStreamSession;
  private List<String> bootstrapServersForJetStreamEvent;
  private String groupIdForLVS;
  private String groupIdForSLC;
  private String groupIdForRNO;
  private String groupIdForLVSDQ;
  private String groupIdForSLCDQ;
  private String groupIdForRNODQ;
  private String groupIdForQA;
  private String groupIdForSession;
  private String groupIdForEvent;
  private String groupIdForBot;
  private String groupIdForCopy;
  private String groupIdForCrossSession;
  private String groupIdForJetStreamNonBotSession;
  private String groupIdForJetStreamNonBotEvent;
  private String groupIdForJetStreamBotSession;
  private String groupIdForJetStreamBotEvent;
  private String topic;
  private String jetStreamNonBotSessionTopic;
  private String jetStreamNonBotEventTopic;
  private String jetStreamBotSessionTopic;
  private String jetStreamBotEventTopic;
}
