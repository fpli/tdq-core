package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentAttributeAccumulator  {
    private AgentAttribute attribute;

    public AgentAttributeAccumulator() {
        this.attribute= new AgentAttribute();
    }
}
