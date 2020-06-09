package com.ebay.sojourner.common.zookeeper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZkConfig {

  private String server;

  private int sessionTimeoutMs;

  private int connectionTimeoutMs;

  private int baseSleepTimeMs;

  private int maxRetries;

  private String namespace;
}
