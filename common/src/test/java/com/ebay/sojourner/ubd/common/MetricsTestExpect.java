package com.ebay.sojourner.ubd.common;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestExpect {

    @JsonProperty("UbiSession")
    private UbiSession ubiSession;
}
