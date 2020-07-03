package com.ebay.sojourner.rt.operators.session;

import com.ebay.sojourner.common.model.IntermediateSession;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class AgentIpFilterFunction extends RichFilterFunction<IntermediateSession> {

  @Override
  public boolean filter(IntermediateSession value) throws Exception {
    String userAgent = value.getUserAgent();
    String ip = value.getIp();
    if ((userAgent != null && ip != null && userAgent.equals("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36") && ip.equals("54.227.162.50"))
        || (userAgent != null && userAgent.equals("GingerClient/0.9.2-RELEASE"))
        || (ip != null && ip.equals("10.195.215.226"))
        || (userAgent != null && ip != null && userAgent.equals("Mozilla/5.0 (Windows; U; Windows NT 5.1; en; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3") && ip.equals("87.182.120.108"))) {
      return true;

    }
    return false;
  }
}
