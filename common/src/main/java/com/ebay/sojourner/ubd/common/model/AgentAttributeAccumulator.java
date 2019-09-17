package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentAttributeAccumulator {
    private AgentAttribute agentAttribute;


    public AgentAttributeAccumulator() {
        this.agentAttribute = new AgentAttribute();

    }
}
