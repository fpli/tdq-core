package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule10 implements Rule<UbiSession> {

    @Override
    public void init() {

    }

    @Override
    public int getBotFlag(UbiSession ubiSession) {
        if (ubiSession.getViCoreCnt() > 15 && (float) ubiSession.getPageCnt() >= 0.45 * ubiSession.getViCoreCnt()) {
            return BotRules.AUTO_CAPTCHA_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }
}
