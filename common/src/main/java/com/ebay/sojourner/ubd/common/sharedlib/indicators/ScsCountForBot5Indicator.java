package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.SessionCoreHelper;

public class ScsCountForBot5Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCountForBot5Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
    } else if (target instanceof IpAttributeAccumulator) {
      IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
      ipAttributeAccumulator.getIpAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target, boolean isNeeded) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore intermediateSession = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot5() < 0) {
        return;
      } else {
        if (isValid(intermediateSession)) {
          if (SessionCoreHelper.isSingleClickSession(intermediateSession)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(intermediateSession, BotRules.SCS_ON_AGENTIP, isNeeded);
          } else {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(intermediateSession, BotRules.SCS_ON_AGENTIP);
          }
        }
      }
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator agentAttributeAccumulator = (AgentIpAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentIpAttribute()
          .merge(agentIpAttribute, BotRules.SCS_ON_AGENTIP);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      int targetFlag = BotRules.SCS_ON_AGENTIP;
      if (botFilter.filter(intermediateSession, targetFlag)) {
        return true;
      }
      return intermediateSession.getBotFlag() > 0 && intermediateSession.getBotFlag() < 200;
    }
    return false;
  }

  private boolean isValid(SessionCore intermediateSession) {
    return !SessionCoreHelper.isNonIframRdtCountZero(intermediateSession)
        && intermediateSession.getIp() != null
        && !SessionCoreHelper.isSingleClickNull(intermediateSession);
  }
}
