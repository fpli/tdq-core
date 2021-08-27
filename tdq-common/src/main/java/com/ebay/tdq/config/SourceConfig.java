package com.ebay.tdq.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Builder
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SourceConfig implements Serializable {

  @JsonProperty(index = 0)
  private String name;
  @JsonProperty(index = 1)
  private String type;
  @JsonProperty(index = 2)
  private Map<String, Object> config;

}
