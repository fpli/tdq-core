package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlinkAppConfig {

  private String nameForFullPipeline;
  private String nameForRTLoadPipeline;
  private String nameForDQPipeline;
  private String nameForHotDeployPipeline;
  private String nameForKafkaSinkPipeline;

  @JsonProperty("source-parallelism")
  private Integer sourceParallelism;

  @JsonProperty("event-parallelism")
  private Integer eventParallelism;

  @JsonProperty("session-parallelism")
  private Integer sessionParallelism;

  @JsonProperty("pre-agentIp-parallelism")
  private Integer preAgentIpParallelism;

  @JsonProperty("agentIp-parallelism")
  private Integer agentIpParallelism;

  @JsonProperty("agent-parallelism")
  private Integer agentParallelism;

  @JsonProperty("ip-parallelism")
  private Integer ipParallelism;

  @JsonProperty("guid-parallelism")
  private Integer guidParallelism;
}
