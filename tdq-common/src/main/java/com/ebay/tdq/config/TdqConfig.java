package com.ebay.tdq.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.Value;

/**
 * @author juntzhang
 */
@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TdqConfig implements Serializable {

  @JsonProperty(index = 0)
  private String id;
  @JsonProperty(index = 1)
  private String name;
  @JsonProperty(index = 2)
  @Singular
  private List<SourceConfig> sources;
  @JsonProperty(index = 3)
  @Singular
  private List<RuleConfig> rules;
  @JsonProperty(index = 4)
  @Singular
  private List<SinkConfig> sinks;
}
