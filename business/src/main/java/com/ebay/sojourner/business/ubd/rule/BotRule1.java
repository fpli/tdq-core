package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;
import java.util.regex.Pattern;

public class BotRule1 extends AbstractBotRule<UbiSession> {

  private static final Pattern pattern =
      Pattern.compile(
          ".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);

  public static void main(String[] args) {
    UbiSession ubiSession = new UbiSession();
    BotRule1 botRule1 = new BotRule1();
    ubiSession.setAgentInfo(
        "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.96 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html),GingerClient/2.9.7-RELEASE");
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
