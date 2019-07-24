package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule203 implements Rule<UbiSession> {

    @Override
    public void init() {

    }

    @Override
    public int getBotFlag(UbiSession session) {
        if (session.getValidPageCnt() == session.getFamilyViCnt() && session.getSiidCnt() == 0) {
            if (session.getValidPageCnt() > 20 && (session.getFirstSiteId() == null || session.getFirstSiteId() != 100)) {
                return BotRules.MANY_VIEW_WITHOUT_SIID;
            }
            if (session.getValidPageCnt() > 100 && session.getFirstSiteId() != null && session.getFirstSiteId() == 100) {
                return BotRules.MANY_VIEW_WITHOUT_SIID;
            }
        }
        return 0;
    }

}
