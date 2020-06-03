package com.ebay.sojourner.common.model;

import java.util.HashMap;
import lombok.Data;

@Data
public class AgentIpAttributeAccumulator {

  private AgentIpAttribute agentIpAttribute;
  private HashMap<Integer, Integer> botFlagStatus = new HashMap<>();

  public AgentIpAttributeAccumulator() {
    this.agentIpAttribute = new AgentIpAttribute();
    botFlagStatus.put(5, 0);
    botFlagStatus.put(8, 0);
  }
}
