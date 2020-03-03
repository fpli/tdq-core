package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
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
    if (source instanceof UbiSession) {
      UbiSession ubiSession = (UbiSession) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot8() >= 0) {
        if (isValid(ubiSession)) {
          if (UbiSessionHelper.isSingleClickSession(ubiSession)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(ubiSession, BotRules.SCS_CONFIRM_ON_AGENTIP, isNeeded);
          } else {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(ubiSession, BotRules.SCS_CONFIRM_ON_AGENTIP);
          }
        }
      }
      if (!UbiSessionHelper.isNonIframRdtCountZero(ubiSession)) {
        if (UbiSessionHelper.isBidBinConfirm(ubiSession)) {
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
    if (source instanceof UbiSession) {
      UbiSession ubiSession = (UbiSession) source;
      int targetFlag = BotRules.SCS_CONFIRM_ON_AGENTIP;
      if (botFilter.filter(ubiSession, targetFlag)) {
        return true;
      }
      return ubiSession.getBotFlag() > 0 && ubiSession.getBotFlag() < 200;
    }
    return false;
  }

  private boolean isValid(UbiSession ubiSession) {
    return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
        && !UbiSessionHelper.isSingleClickNull(ubiSession);
  }
}
