package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IsValidIPv4;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentStringMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  public static final String SHOCKWAVE_FLASH_AGENT = "Shockwave Flash";
  public static final int AGENT_MAX_LENGTH = 2000;
  private static Set<Integer> agentExcludeSet;

  @Override
  public void init() throws Exception {
    agentExcludeSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
    log.info(
        "UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES): {}",
        UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES));
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    // do clear first as end method may not been invoked.
    sessionAccumulator.getUbiSession().setAgentString(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    // Same logic implemented in IntermediaEventMetrics.java -> Line289
    String agentInfo = event.getAgentInfo();
    // logger.info("agentExcludeSet.size():"+agentExcludeSet.size());
    if (!event.isRdt()
        && !event.isIframe()
        && !agentExcludeSet.contains(event.getPageId())
        && agentInfo != null
        && !agentInfo.equals(SHOCKWAVE_FLASH_AGENT)
        && !IsValidIPv4.isValidIP(agentInfo)) {
      if (agentInfo.length() > AGENT_MAX_LENGTH) {
        agentInfo = agentInfo.substring(0, AGENT_MAX_LENGTH);
      }

      if (sessionAccumulator.getUbiSession().getAgentString() == null) {
        sessionAccumulator.getUbiSession().setAgentString(agentInfo);
      }
      if (sessionAccumulator.getUbiSession().getAgentSets() != null
          && sessionAccumulator.getUbiSession().getAgentSets().size() < 2) {
        sessionAccumulator.getUbiSession().getAgentSets().add(agentInfo);
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
    int agentCnt = 0;
    if (!sessionAccumulator.getUbiSession().getAgentSets().isEmpty()) {
      // Use the first agent String
      //            sessionAccumulator.getUbiSession().setAgentString(agentString);
      agentCnt = sessionAccumulator.getUbiSession().getAgentSets().size();
    }
    sessionAccumulator.getUbiSession().setAgentCnt(agentCnt);
  }
}
