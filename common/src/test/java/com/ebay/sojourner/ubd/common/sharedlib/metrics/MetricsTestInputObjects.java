package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestInputObjects {

    @JsonProperty("UbiEvent")
    private UbiEvent ubiEvent;

    @JsonProperty("SessionAccumulator")
    private SessionAccumulator sessionAccumulator;
}
