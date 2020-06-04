package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectIPIPIndicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public SuspectIPIPIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
      IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
      ipAttributeAccumulator.getIpAttribute().clear();
  }

  @Override
  public void feed(Source source, Target target) throws Exception {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
      ipAttributeAccumulator
          .getIpAttribute()
          .feed(agentIpAttribute, BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    return false;
  }
}