package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AgentIpAttributeAccumulator {

  private AgentIpAttribute agentIpAttribute;
  private Map<Integer, Integer> signatureStates = new HashMap<>();

  public AgentIpAttributeAccumulator() {
    this.agentIpAttribute = new AgentIpAttribute();
    signatureStates.put(5, 0);
    signatureStates.put(8, 0);
  }
}
