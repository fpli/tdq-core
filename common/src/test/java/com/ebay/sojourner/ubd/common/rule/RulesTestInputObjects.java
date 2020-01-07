package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RulesTestInputObjects {
    @JsonProperty("UbiEvent")
    private UbiEvent ubiEvent;

    @JsonProperty("UbiSession")
    private UbiSession ubiSession;

    @JsonProperty("AgentIpAttribute")
    private AgentIpAttribute agentIpAttribute;

    @JsonProperty("AgentAttribute")
    private AgentAttribute agentAttribute;

    @JsonProperty("IpAttribute")
    private IpAttribute ipAttribute;
}
