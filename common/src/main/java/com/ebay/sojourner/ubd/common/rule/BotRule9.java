package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule9 extends AbstractBotRule<UbiSession> {

    public static final int SEARCH_VIEW_BOUND = 400;

    @Override
    public int getBotFlag(UbiSession ubiSession) {
        if ((ubiSession.getSearchCnt() > SEARCH_VIEW_BOUND && ubiSession.getViewCnt() > 0) || (ubiSession.getViewCnt() > SEARCH_VIEW_BOUND && ubiSession.getSearchCnt() > 0)) {
            return BotRules.MANY_SEARCH_VIEW_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }
}
