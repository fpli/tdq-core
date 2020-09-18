package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectIPIPIndicator extends
    AbstractIndicator<AgentIpAttribute, IpAttributeAccumulator> {

  public SuspectIPIPIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(IpAttributeAccumulator ipAttributeAccumulator) throws Exception {
      ipAttributeAccumulator.getIpAttribute().clear();
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute,
                   IpAttributeAccumulator ipAttributeAccumulator) throws Exception {
      ipAttributeAccumulator
          .getIpAttribute()
          .feed(agentIpAttribute, BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Override
  public boolean filter(AgentIpAttribute source, IpAttributeAccumulator target) throws Exception {
    return false;
  }
}
