package com.ebay.sojourner.ubd.rt.util;

import java.util.List;
import lombok.Data;

@Data
public class KafkaConfig {

  private List<String> bootstrapServersForRNO;
  private List<String> bootstrapServersForSLC;
  private List<String> bootstrapServersForLVS;
  private List<String> bootstrapServersForQA;
  private String groupIdForLVS;
  private String groupIdForSLC;
  private String groupIdForRNO;
  private String groupIdForQA;
  private String topic;
}
