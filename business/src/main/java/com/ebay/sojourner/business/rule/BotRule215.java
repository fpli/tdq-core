package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule215 extends AbstractBotRule<UbiSession> {

  @Override
  public int getBotFlag(UbiSession session) {
    int validPageCount = session.getValidPageCnt();
    int grCount = session.getGrCnt();
    if (validPageCount > 400
        && (session.getViCnt() * 1.0f) > (0.97f * validPageCount)
        && session.getViCnt() > (grCount * 50)) {
      return BotRules.HIGH_DENSITY_VIEWS;
    } else {
      return 0;
    }
  }
}
