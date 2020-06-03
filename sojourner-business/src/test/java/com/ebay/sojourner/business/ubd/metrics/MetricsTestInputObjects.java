package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestInputObjects {

  @JsonProperty("UbiEvent")
  private UbiEvent ubiEvent;

  @JsonProperty("SessionAccumulator")
  private SessionAccumulator sessionAccumulator;
}
