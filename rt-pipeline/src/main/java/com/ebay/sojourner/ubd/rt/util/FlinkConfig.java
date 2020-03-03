package com.ebay.sojourner.ubd.rt.util;

import lombok.Data;

@Data
public class FlinkConfig {
  public FlinkAppConfig app;
  public FlinkCheckpointConfig checkpoint;
}
