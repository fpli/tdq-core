package com.ebay.sojourner.ubd.common.model;

public class AgentIpAttributeAccumulator  {
    private AgentIpAttribute agentIpAttribute;
    private UbiSession ubiSession;


    public AgentIpAttributeAccumulator() {
        this.ubiSession = new UbiSession();
        this.agentIpAttribute= new AgentIpAttribute();
    }
}
