package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentIpAttributeAccumulator  {
    private AgentIpAttribute attribute;
    private UbiSession ubiSession;


    public AgentIpAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.attribute= new AgentIpAttribute();
    }
}
