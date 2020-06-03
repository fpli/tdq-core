package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentHash;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.util.TransformUtil;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class AgentIpFilterFunction extends RichFilterFunction<SessionCore> {

  @Override
  public boolean filter(SessionCore sessionCore) throws Exception {

    if (sessionCore.getIp() != null && sessionCore.getUserAgent() != null) {
      long[] agentHashArr = TransformUtil.md522Long("Mozilla/5.0 (Windows NT 6.1; WOW64) A"
          + "ppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b");
      AgentHash agentHash = new AgentHash();
      agentHash.setAgentHash1(agentHashArr[0]);
      agentHash.setAgentHash2(agentHashArr[1]);
      return sessionCore.getIp().equals(TransformUtil.ipToInt("40.77.189.106"))
          && sessionCore.getUserAgent()
          .equals(agentHash);
    }

    return false;
  }

}
