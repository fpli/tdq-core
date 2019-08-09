package com.ebay.sojourner.ubd.common.model;

public class AgentIpAttributeAccumulator extends AttributeAccumulator<AgentIpAttribute> {
    public AgentIpAttributeAccumulator() {
        super();
        this.setAttribute(new AgentIpAttribute());
    }
}
