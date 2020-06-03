package com.ebay.sojourner.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class AgentIpSignature implements Signature, Serializable {

  private Map<AgentIpAttribute, Set<Integer>> agentIpBotSignature = new HashMap<>();

  public AgentIpSignature() {
  }
}
