package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;

public class NewBotIndicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public NewBotIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator =
        (AgentIpAttributeAccumulator) target;
    agentIpAttributeAccumulator.getAgentIpAttribute().clear();

  }

  @Override
  public void feed(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator
          .getAgentIpAttribute()
          .feed(sessionCore, BotRules.SAME_AGENT_IP);
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator agentAttributeAccumulator = (AgentIpAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentIpAttribute()
          .merge(agentIpAttribute, BotRules.SAME_AGENT_IP);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      int targetFlag = BotRules.SAME_AGENT_IP;
      if (SessionCoreHelper.getExInternalIp(sessionCore) == null) {
        return true;
      }
      if (botFilter.filter(sessionCore, targetFlag)) {
        return true;
      }
      return sessionCore.getBotFlag() == null && sessionCore.getBotFlag() > 0
          && sessionCore.getBotFlag() <= 200;
    }
    return false;
  }

  private boolean isValid(SessionCore sessionCore) {
    return !SessionCoreHelper.isNonIframRdtCountZero(sessionCore)
        && sessionCore.getIp() != null
        && !SessionCoreHelper.isSingleClickNull(sessionCore);
  }
}
