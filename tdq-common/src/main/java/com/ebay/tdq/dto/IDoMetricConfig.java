package com.ebay.tdq.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IDoMetricConfig {
  private String metricName;
  private String window;
  private List<String> dimensions;
  private String operator;
  private List<String> expressions;
  private String filter;
  // https://wiki.vip.corp.ebay.com/display/X/Pathfinder+API+Reference
  private Map<String, FieldType> dataTypes;
  private String comment;

  public String cast(String field, String originalFiled) {
    FieldType fieldType = dataTypes.get(originalFiled);
    if (fieldType != null) {
      return "cast(" + field + " AS " + fieldType.name() + ")";
    }
    return field;
  }

  public enum FieldType {
    STRING, LONG, INTEGER, DOUBLE, BOOLEAN, DATE, TIMESTAMP
  }
}
