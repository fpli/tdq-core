package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class ScsCntForBot7IPIndicator extends
    AbstractIndicator<AgentIpAttribute, IpAttributeAccumulator> {

  public ScsCntForBot7IPIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(IpAttributeAccumulator ipAttributeAccumulator) throws Exception {
    ipAttributeAccumulator.getIpAttribute().clear();
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute,
                   IpAttributeAccumulator ipAttributeAccumulator) throws Exception {
    ipAttributeAccumulator.getIpAttribute().feed(agentIpAttribute, BotRules.SCS_ON_IP);
  }

  @Override
  public boolean filter(AgentIpAttribute source, IpAttributeAccumulator target) throws Exception {
    return false;
  }
}
