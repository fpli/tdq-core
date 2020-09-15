package com.ebay.sojourner.common.model.attribute;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AttributeTestInputObjects {

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;

  @JsonProperty("AgentIpAttribute")
  private AgentIpAttribute agentIpAttribute;

  @JsonProperty("SessionCore")
  private SessionCore sessionCore;

  private int botFlag;

  private boolean needed;

  public void setNeeded(boolean needed) {
    this.needed = needed;
  }
}
