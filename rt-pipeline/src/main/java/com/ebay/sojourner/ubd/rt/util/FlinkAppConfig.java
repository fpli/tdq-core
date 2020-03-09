package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlinkAppConfig {

  private String name;

  @JsonProperty("source-parallelism")
  private Integer sourceParallelism;

  @JsonProperty("event-parallelism")
  private Integer eventParallelism;
}
