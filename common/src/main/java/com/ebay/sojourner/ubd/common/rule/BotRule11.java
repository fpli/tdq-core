package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.*;

import java.io.InputStream;

public class BotRule11 implements Rule<UbiSession> {

    private static BotFilter botFilter ;

    @Override
    public void init() {
        InputStream resourceAsStream = BotRule11.class.getResourceAsStream("/ubi.proprties");
        botFilter = new UbiBotFilter(UBIConfig.getInstance(resourceAsStream));
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
