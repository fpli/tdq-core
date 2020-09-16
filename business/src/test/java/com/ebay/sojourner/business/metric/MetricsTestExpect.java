package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestExpect {

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;
}
