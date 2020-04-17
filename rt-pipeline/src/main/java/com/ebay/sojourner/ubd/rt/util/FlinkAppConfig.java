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
  private String nameForKafkaCopyPipeline;

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

  @JsonProperty("copy-kafka-parallelism")
  private Integer copyKafkaParallelism;

  @JsonProperty("event-kafka-parallelism")
  private Integer eventKafkaParallelism;

  @JsonProperty("session-kafka-parallelism")
  private Integer sessionKafkaParallelism;

  @JsonProperty("bot-kafka-parallelism")
  private Integer botKafkaParallelism;

  @JsonProperty("broadcast-parallelism")
  private Integer broadcastParallelism;

  @JsonProperty("metrics-parallelism")
  private Integer metricsParallelism;
}
