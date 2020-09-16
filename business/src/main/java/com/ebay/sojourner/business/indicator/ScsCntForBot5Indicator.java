package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScsCntForBot5Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCntForBot5Indicator(BotFilter botFilter) {
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
  public void feed(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot5() < 0) {
        return;
      } else {
        if (isValid(sessionCore)) {
          if (SessionCoreHelper.isSingleClickSession(sessionCore)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(sessionCore, BotRules.SCS_ON_AGENTIP);
          } else {
            //            log.error("BOT5-----singleclicksessionFlag is false:"+sessionCore);
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(sessionCore, BotRules.SCS_ON_AGENTIP);
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
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      int targetFlag = BotRules.SCS_ON_AGENTIP;
      if (botFilter.filter(sessionCore, targetFlag)) {
        return true;
      }
      return sessionCore.getBotFlag() > 0 && sessionCore.getBotFlag() < 200;
    }
    return false;
  }

  private boolean isValid(SessionCore sessionCore) {
    return !SessionCoreHelper.isNonIframRdtCountZero(sessionCore)
        && sessionCore.getIp() != null
        && !SessionCoreHelper.isSingleClickNull(sessionCore);
  }
}
