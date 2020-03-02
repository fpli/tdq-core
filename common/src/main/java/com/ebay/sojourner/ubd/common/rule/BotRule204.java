package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule204 extends AbstractBotRule<UbiSession> {

  @Override
  public int getBotFlag(UbiSession session) {
    int validPageCount = session.getValidPageCnt();
    if (session.getGrCnt() == validPageCount && session.getSiidCnt() == 0) {
      if ((validPageCount > 20
              && (session.getFirstSiteId() == Integer.MIN_VALUE || session.getFirstSiteId() != 100))
          || (validPageCount > 100
              && session.getFirstSiteId() != Integer.MIN_VALUE
              && session.getFirstSiteId() == 100)) {
        return BotRules.MANY_SRP_WITHOUT_SIID;
      }
    }
    return 0;
  }
}
