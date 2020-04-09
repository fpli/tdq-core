package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;

public class BotRule11 extends AbstractBotRule<UbiSession> {

  private BotFilter botFilter;

  private volatile UbiSessionHelper ubiSessionHelper;

  @Override
  public void init() {
    botFilter = new UbiBotFilter();
    ubiSessionHelper = new UbiSessionHelper();
  }

  @Override
  public int getBotFlag(UbiSession ubiSession) throws InterruptedException {
    if (!filter(ubiSession)) {
      if (ubiSessionHelper.isIabAgent(ubiSession)) {
        return BotRules.SPECIFIC_SPIDER_IAB;
      } else {
        return BotRules.NON_BOT_FLAG;
      }
    }
    return BotRules.NON_BOT_FLAG;
  }

  private boolean filter(UbiSession ubiSession) throws InterruptedException {
    if (botFilter.filter(ubiSession, BotRules.SPECIFIC_SPIDER_IAB)) {
      return true;
    }

    return ubiSession.getBotFlag() > 0;

  }
}
