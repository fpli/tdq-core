package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentAttributeAccumulator {
    private AgentAttribute agentAttribute;
    private UbiSession ubiSession;

    public AgentAttributeAccumulator() {
        this.agentAttribute = new AgentAttribute();
        this.ubiSession = new UbiSession();
    }
}
