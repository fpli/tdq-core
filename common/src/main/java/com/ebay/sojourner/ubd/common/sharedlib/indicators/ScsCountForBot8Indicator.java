package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;

public class ScsCountForBot8Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCountForBot8Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_CONFIRM_ON_AGENTIP);
    } else if (target instanceof IpAttributeAccumulator) {
      IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
      ipAttributeAccumulator.getIpAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target, boolean isNeeded) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot8() >= 0) {
        if (isValid(intermediateSession)) {
          if (UbiSessionHelper.isSingleClickSession(intermediateSession)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(intermediateSession, BotRules.SCS_CONFIRM_ON_AGENTIP, isNeeded);
          } else {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(intermediateSession, BotRules.SCS_CONFIRM_ON_AGENTIP);
          }
        }
      }
      if (!UbiSessionHelper.isNonIframRdtCountZero(intermediateSession)) {
        if (UbiSessionHelper.isBidBinConfirm(intermediateSession)) {
          agentIpAttributeAccumulator
              .getAgentIpAttribute()
              .setBbcCount(agentIpAttributeAccumulator.getAgentIpAttribute().getBbcCount() + 1);
        }
      }
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator agentAttributeAccumulator = (AgentIpAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentIpAttribute()
          .merge(agentIpAttribute, BotRules.SCS_CONFIRM_ON_AGENTIP);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      int targetFlag = BotRules.SCS_CONFIRM_ON_AGENTIP;
      if (botFilter.filter(intermediateSession, targetFlag)) {
        return true;
      }
      return intermediateSession.getBotFlag() > 0 && intermediateSession.getBotFlag() < 200;
    }
    return false;
  }

  private boolean isValid(IntermediateSession intermediateSession) {
    return !UbiSessionHelper.isNonIframRdtCountZero(intermediateSession)
        && !UbiSessionHelper.isSingleClickNull(intermediateSession);
  }
}
