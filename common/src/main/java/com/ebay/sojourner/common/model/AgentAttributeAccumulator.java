package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AgentAttributeAccumulator {

  private AgentAttribute agentAttribute;
  private Map<Integer, Integer> signatureStates = new HashMap<>();

  public AgentAttributeAccumulator() {
    this.agentAttribute = new AgentAttribute();
    signatureStates.put(6, 0);
    signatureStates.put(220, 0);
    signatureStates.put(221, 0);
  }
}
