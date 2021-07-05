package com.ebay.tdq.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Builder
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TransformationConfig implements Serializable {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String alias;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ExpressionConfig expression;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String expr;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String filter;
}
