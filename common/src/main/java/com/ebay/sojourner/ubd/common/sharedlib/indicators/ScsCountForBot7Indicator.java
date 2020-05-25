package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.TransformUtil;
import com.ebay.sojourner.ubd.common.util.SessionCoreHelper;

public class ScsCountForBot7Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCountForBot7Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_ON_IP);
    } else if (target instanceof IpAttributeAccumulator) {
      IpAttributeAccumulator agentIpAttributeAccumulator = (IpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getIpAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target, boolean isNeeded) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore intermediateSession = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot7() >= 0) {
        if (isValid(intermediateSession)) {
          if (SessionCoreHelper.isSingleClickSession(intermediateSession)) {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .feed(intermediateSession, BotRules.SCS_ON_IP, isNeeded);
          } else {
            agentIpAttributeAccumulator
                .getAgentIpAttribute()
                .revert(intermediateSession, BotRules.SCS_ON_IP);
          }
        }
      }
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
      ipAttributeAccumulator.getIpAttribute().feed(agentIpAttribute, BotRules.SCS_ON_IP, isNeeded);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      int targetFlag = BotRules.SCS_ON_IP;
      if (botFilter.filter(intermediateSession, targetFlag)) {
        return true;
      }
      if (intermediateSession.getBotFlag() > 0 && intermediateSession.getBotFlag() < 200) {
        return true;
      }
      return intermediateSession.getIp() == null;
    }
    return false;
  }

  private boolean isValid(SessionCore intermediateSession) {
    return !SessionCoreHelper.isNonIframRdtCountZero(intermediateSession)
        && !isIpBlank(TransformUtil.int2Ip(intermediateSession.getIp()))
        && !SessionCoreHelper.isSingleClickNull(intermediateSession);
  }

  protected boolean isAgentBlank(String agent) {
    return agent == null || "".equals(agent);
  }
  protected boolean isIpBlank(String ip) {
    return ip == null || "".equals(ip);
  }
}
