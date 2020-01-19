package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;

public class BotRule11 implements Rule<UbiSession> {

    private static BotFilter botFilter ;

    @Override
    public void init() {
        botFilter = new UbiBotFilter();
    }

    @Override
    public int getBotFlag(UbiSession ubiSession) {
        if(!filter(ubiSession)) {
            if (UbiSessionHelper.isIabAgent(ubiSession)) {
                return BotRules.SPECIFIC_SPIDER_IAB;
            } else {
                return BotRules.NON_BOT_FLAG;
            }
        }
        return BotRules.NON_BOT_FLAG;
    }

    private boolean filter(UbiSession ubiSession ) {
        if (botFilter.filter(ubiSession, BotRules.SPECIFIC_SPIDER_IAB)) {
            return true;
        }

        if (ubiSession.getBotFlag() > 0) {
            return true;
        }

        return false;
    }
}
