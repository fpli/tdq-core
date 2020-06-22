package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AgentAttributeAccumulator {

  private AgentAttribute agentAttribute;
  private Map<Integer, SignatureInfo> signatureStatus ;

  public AgentAttributeAccumulator() {
    this.agentAttribute = new AgentAttribute();
    this.signatureStatus = new HashMap<>();
  }
}
