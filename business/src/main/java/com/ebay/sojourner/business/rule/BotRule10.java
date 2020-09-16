package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule10 extends AbstractBotRule<UbiSession> {

  @Override
  public int getBotFlag(UbiSession ubiSession) {
    if (ubiSession.getViCoreCnt() > 15
        && (float) ubiSession.getPageCnt() >= 0.45 * ubiSession.getViCoreCnt()) {
      return BotRules.AUTO_CAPTCHA_BOT_FLAG;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }
}
