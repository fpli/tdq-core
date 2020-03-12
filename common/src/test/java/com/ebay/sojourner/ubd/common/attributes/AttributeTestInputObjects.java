package com.ebay.sojourner.ubd.common.attributes;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AttributeTestInputObjects {

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;

  @JsonProperty("AgentIpAttribute")
  private AgentIpAttribute agentIpAttribute;

  private int botFlag;

  private boolean needed;

  public void setNeeded(boolean needed) {
    this.needed = needed;
  }
}
