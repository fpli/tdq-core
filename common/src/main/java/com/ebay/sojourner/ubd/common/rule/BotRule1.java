package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;
import java.util.regex.Pattern;

public class BotRule1 extends AbstractBotRule<UbiSession> {

  private static final Pattern pattern =
      Pattern.compile(
          ".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);

  public static void main(String[] args) {
    UbiSession ubiSession = new UbiSession();
    BotRule1 botRule1 = new BotRule1();
    ubiSession.setAgentInfo(
        "GingerClient/2.9.7-RELEASE");
    System.out.println(botRule1.detectSpiderAgent(ubiSession));
  }

  private int detectSpiderAgent(UbiSession ubiSession) {
    String agentInfo = ubiSession.getAgentInfo();
    if (agentInfo != null && pattern.matcher(agentInfo).matches() && !agentInfo.toUpperCase()
        .contains("CUBOT")) {
      return BotRules.SPIDER_BOT_FLAG;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }

  @Override
  public int getBotFlag(UbiSession ubiSession) {
    return detectSpiderAgent(ubiSession);
  }
}
