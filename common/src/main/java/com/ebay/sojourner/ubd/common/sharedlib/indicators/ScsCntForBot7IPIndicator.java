package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class ScsCntForBot7IPIndicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCntForBot7IPIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    IpAttributeAccumulator agentIpAttributeAccumulator = (IpAttributeAccumulator) target;
    agentIpAttributeAccumulator.getIpAttribute().clear();
  }

  @Override
  public void feed(Source source, Target target) throws Exception {
    AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
    IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
    ipAttributeAccumulator.getIpAttribute().feed(agentIpAttribute, BotRules.SCS_ON_IP);
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    return false;
  }
}