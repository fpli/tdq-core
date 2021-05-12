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

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RuleConfig implements Serializable {
  @JsonProperty(index = 0)
  private String name;
  @JsonProperty(index = 1)
  private String type;
  @JsonProperty(index = 2)
  private Map<String, Object> config;
  @JsonProperty(index = 3)
  @Singular
  private List<ProfilerConfig> profilers;

}
