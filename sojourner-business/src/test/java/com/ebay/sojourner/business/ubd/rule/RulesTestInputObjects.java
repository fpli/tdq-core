package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.GuidAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RulesTestInputObjects {

  @JsonProperty("UbiEvent")
  private UbiEvent ubiEvent;

  @JsonProperty("UbiSession")
  private UbiSession ubiSession;

  @JsonProperty("AgentIpAttribute")
  private AgentIpAttribute agentIpAttribute;

  @JsonProperty("AgentAttribute")
  private AgentAttribute agentAttribute;

  @JsonProperty("IpAttribute")
  private IpAttribute ipAttribute;

  @JsonProperty("GuidAttribute")
  private GuidAttribute guidAttribute;
}
