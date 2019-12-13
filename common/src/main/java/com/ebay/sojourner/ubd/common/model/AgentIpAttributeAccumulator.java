package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentIpAttributeAccumulator {
    private AgentIpAttribute agentIpAttribute;


    public AgentIpAttributeAccumulator() {
        this.agentIpAttribute = new AgentIpAttribute();

    }
}
