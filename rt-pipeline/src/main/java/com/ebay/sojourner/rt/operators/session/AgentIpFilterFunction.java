package com.ebay.sojourner.rt.operators.session;

import com.ebay.sojourner.common.model.IntermediateSession;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class AgentIpFilterFunction extends RichFilterFunction<IntermediateSession> {

  @Override
  public boolean filter(IntermediateSession value) throws Exception {
    String userAgent = value.getUserAgent();
    String ip = value.getIp();
    if ((userAgent != null && ip != null && userAgent.equals("GingerClient/2.9.7-RELEASE") && ip.equals("10.76.235.25"))
        || (userAgent != null && userAgent.equals(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11) AppleWebKit/601.1.56 (KHTML, like Gecko) Version/9.0 Safari/601.1.56"))
        || (ip != null && ip.equals("109.102.111.61"))
        || (userAgent != null && userAgent.equals("GingerClient/0.9.2-RELEASE"))
        || (ip != null && ip.equals("10.105.221.236"))
        || (userAgent != null && userAgent.equals(
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3"))
        || (ip != null && ip.equals("93.198.216.137"))) {
      return true;

    }
    return false;
  }
}
