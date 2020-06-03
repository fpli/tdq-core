package com.ebay.sojourner.common.attributes;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AttributeTestInputObjects {

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;

  @JsonProperty("AgentIpAttribute")
  private AgentIpAttribute agentIpAttribute;

  @JsonProperty("IntermediateSession")
  private IntermediateSession intermediateSession;

  private int botFlag;

  private boolean needed;

  public void setNeeded(boolean needed) {
    this.needed = needed;
  }
}
