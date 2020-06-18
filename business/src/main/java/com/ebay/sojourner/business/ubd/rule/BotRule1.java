package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;
import java.util.regex.Pattern;

public class BotRule1 extends AbstractBotRule<UbiSession> {

  private static final Pattern pattern =
      Pattern.compile(
          ".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);

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
