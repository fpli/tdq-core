package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AgentAttributeAccumulator {

  private AgentAttribute agentAttribute;
  private Map<Integer, Integer> signatureStatus = new HashMap<>();

  public AgentAttributeAccumulator() {
    this.agentAttribute = new AgentAttribute();
    signatureStatus.put(6, 0);
    signatureStatus.put(220, 0);
    signatureStatus.put(221, 0);
  }
}
