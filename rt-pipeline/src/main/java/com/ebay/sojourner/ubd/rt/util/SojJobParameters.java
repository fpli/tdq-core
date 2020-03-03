package com.ebay.sojourner.ubd.rt.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.common.ExecutionConfig;

public class SojJobParameters extends ExecutionConfig.GlobalJobParameters {
  private static final long serialVersionUID = -1L;
  private Map<String, String> parameters;

  public SojJobParameters() {
    this.parameters = new HashMap<>();
    this.parameters.put("hello", "world");
  }

  @Override
  public Map<String, String> toMap() {
    return parameters;
  }
}
