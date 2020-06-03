package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule212 extends AbstractBotRule<UbiSession> {

  @Override
  public int getBotFlag(UbiSession session) {
    if (session.getValidPageCnt() > 3000) {
      return BotRules.MANY_VALID_PAGE;
    } else {
      return 0;
    }
  }
}
