package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestExpect {

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;
}
