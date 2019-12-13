package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule215 implements Rule<UbiSession> {

    @Override
    public void init() {

    }

    @Override
    public int getBotFlag(UbiSession session) {
        int validPageCount = session.getValidPageCnt();
        int grCount = session.getGrCnt();
        if (validPageCount > 400 && (session.getViCnt() * 1.0f) > (0.97f * validPageCount) && session.getViCnt() > (grCount * 50)) {
            return BotRules.HIGH_DENSITY_VIEWS;
        } else {
            return 0;
        }
    }

}