package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.BotRules;
import java.util.regex.Pattern;

public class BotRule1 extends AbstractBotRule<UbiEvent> {

  private static final Pattern pattern =
      Pattern.compile(
          ".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);

  private int detectSpiderAgent(UbiEvent event) {
    String agentInfo = event.getAgentInfo();
    if (agentInfo != null && pattern.matcher(agentInfo).matches() && !agentInfo.toUpperCase()
        .contains("CUBOT")) {
      return BotRules.SPIDER_BOT_FLAG;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }

  @Override
  public int getBotFlag(UbiEvent ubiEvent) {
    return detectSpiderAgent(ubiEvent);
  }

  public static void main(String[] args) {
    UbiEvent ubiEvent = new UbiEvent();
    BotRule1 botRule1 = new BotRule1();
    ubiEvent.setAgentInfo("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.96 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    System.out.println(botRule1.detectSpiderAgent(ubiEvent));
  }
}
