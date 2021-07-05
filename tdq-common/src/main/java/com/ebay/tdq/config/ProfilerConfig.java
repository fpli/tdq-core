package com.ebay.tdq.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProfilerConfig implements Serializable {
  @JsonProperty(value = "metric-name", index = 0)
  private String metricName;
  @JsonProperty(index = 1)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String expr;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ExpressionConfig expression;
  @JsonProperty(index = 2)
  private String filter;
  @JsonProperty(index = 3)
  @Singular
  private List<TransformationConfig> transformations;
  @JsonProperty(index = 4)
  @Singular
  private List<String> dimensions;
  @JsonProperty(index = 5)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String comment;
  @JsonProperty(index = 6)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Object> config;
}