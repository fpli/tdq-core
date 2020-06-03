package com.ebay.sojourner.common.model;

import java.util.HashMap;
import lombok.Data;

@Data
public class AgentAttributeAccumulator {

  private AgentAttribute agentAttribute;
  private HashMap<Integer, Integer> botFlagStatus = new HashMap<>();

  public AgentAttributeAccumulator() {
    this.agentAttribute = new AgentAttribute();
    botFlagStatus.put(6, 0);
    botFlagStatus.put(220, 0);
    botFlagStatus.put(221, 0);
  }
}
