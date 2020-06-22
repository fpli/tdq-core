package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class AgentIpAttributeAccumulator {

  private AgentIpAttribute agentIpAttribute;
  private Map<Integer, SignatureInfo> signatureStatus;

  public AgentIpAttributeAccumulator() {
    this.agentIpAttribute = new AgentIpAttribute();
    this.signatureStatus = new HashMap<>();
  }
}
