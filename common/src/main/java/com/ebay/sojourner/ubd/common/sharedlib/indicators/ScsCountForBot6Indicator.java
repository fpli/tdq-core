package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;

public class ScsCountForBot6Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCountForBot6Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_ON_AGENT);
      agentIpAttributeAccumulator.getAgentIpAttribute().setIpCount(0);
    } else if (target instanceof AgentAttributeAccumulator) {
      AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
      agentAttributeAccumulator.getAgentAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target, boolean isNeeded) throws Exception {

    if (source instanceof UbiSession) {
      UbiSession ubiSession = (UbiSession) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot6() < 0) {

      } else {
        if (isValid(ubiSession)) {
          if (UbiSessionHelper.isSingleClickSession(ubiSession)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(ubiSession, BotRules.SCS_ON_AGENT, isNeeded);
          } else {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(ubiSession, BotRules.SCS_ON_AGENT);
          }
        }
      }

      if (UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
          && !isIpBlank(ubiSession.getIp())
          && agentIpAttributeAccumulator.getAgentIpAttribute().getIpCount() <= 0) {
        agentIpAttributeAccumulator.getAgentIpAttribute().setIpCount(1);
      }
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentAttribute()
          .feed(agentIpAttribute, BotRules.SCS_ON_AGENT, isNeeded);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof UbiSession) {
      UbiSession ubiSession = (UbiSession) source;
      int targetFlag = BotRules.SCS_ON_AGENT;
      if (botFilter.filter(ubiSession, targetFlag)) {
        return true;
      }
      if (ubiSession.getBotFlag() > 0 && ubiSession.getBotFlag() < 200) {
        return true;
      }
      return ubiSession.getUserAgent() == null;
    }
    return false;
  }

  private boolean isValid(UbiSession ubiSession) {
    return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
        && !isIpBlank(ubiSession.getIp())
        && !UbiSessionHelper.isSingleClickNull(ubiSession);
  }

  protected boolean isIpBlank(String ip) {
    return ip == null || "".equals(ip);
  }
}
