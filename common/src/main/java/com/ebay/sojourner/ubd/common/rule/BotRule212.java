package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule212 implements Rule<UbiSession> {

    @Override
    public void init() {

    }

    @Override
    public int getBotFlag(UbiSession session) {
        if (session.getValidPageCnt() > 3000) {
            return BotRules.MANY_VALID_PAGE;
        } else {
            return 0;
        }
    }

}
