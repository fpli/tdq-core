package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentIpAttributeAccumulator  {
    private AgentIpAttribute attribute;


    public AgentIpAttributeAccumulator() {
        this.attribute= new AgentIpAttribute();
    }
}
