package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

@Data
public class AgentAttributeAccumulator  {
    private AgentAttribute attribute;
    private UbiSession ubiSession;


    public AgentAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.attribute= new AgentAttribute();
    }
}
