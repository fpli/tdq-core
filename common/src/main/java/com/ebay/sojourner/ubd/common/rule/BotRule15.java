package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule15 implements Rule<UbiSession> {
    public static final int MANY_EVENTS_BOUND = 10000;

    @Override
    public int getBotFlag(UbiSession ubiSession) {
        if (ubiSession.getAbsEventCnt() >= MANY_EVENTS_BOUND) {
            return BotRules.MANY_EVENTS_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }

    @Override
    public void init() {

    }

}
