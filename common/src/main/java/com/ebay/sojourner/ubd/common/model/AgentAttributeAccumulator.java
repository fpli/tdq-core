package com.ebay.sojourner.ubd.common.model;

public class AgentAttributeAccumulator extends AttributeAccumulator<AgentAttribute> {
    public AgentAttributeAccumulator() {
        super();
        this.setAttribute(new AgentAttribute());
    }
}
