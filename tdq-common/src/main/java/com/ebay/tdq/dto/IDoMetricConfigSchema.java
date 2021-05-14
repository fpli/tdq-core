package com.ebay.tdq.dto;

import com.ebay.tdq.config.TransformationConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class IDoMetricConfigSchema {
  @JsonProperty(value = "field_patterns", index = 0)
  private Map<String, String> fieldPatterns;
  private Map<String, TransformationConfig> transformations;
  private Map<String, Aggregate> aggregates;

  @Data
  public static class Aggregate {
    private String expr;
    private List<TransformationConfig> params;
  }
}
