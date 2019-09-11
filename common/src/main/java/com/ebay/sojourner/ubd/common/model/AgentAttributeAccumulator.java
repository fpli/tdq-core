package com.ebay.sojourner.ubd.common.model;

public class AgentAttributeAccumulator  {
    private AgentAttribute agentAttribute;
    private UbiSession ubiSession;


    public AgentAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.agentAttribute= new AgentAttribute();
    }
}
