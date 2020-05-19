package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class AgentIpFilterFunction extends RichFilterFunction<IntermediateSession> {

  @Override
  public boolean filter(IntermediateSession intermediateSession) throws Exception {

    if (intermediateSession.getClientIp() != null && intermediateSession.getUserAgent() != null) {
      return intermediateSession.getClientIp().equals("40.77.189.106")
          && intermediateSession.getUserAgent()
          .equals("Mozilla/5.0 (Windows NT 6.1; WOW64) A"
              + "ppleWebKit/534+ (KHTML, like Gecko) BingPreview/1.0b");
    }

    return false;
  }
}
