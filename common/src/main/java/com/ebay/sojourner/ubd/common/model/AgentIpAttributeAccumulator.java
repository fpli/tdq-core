package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentIpAttributeAccumulator {
    private AgentIpAttribute agentIpAttribute;
    private UbiSession ubiSession;

    public AgentIpAttributeAccumulator() {
        this.agentIpAttribute = new AgentIpAttribute();
        this.ubiSession = new UbiSession();
    }
}
