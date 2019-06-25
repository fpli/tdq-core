package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;

import static com.ebay.sojourner.ubd.common.util.BotRules.*;

public class BotRule7 implements Rule<IpAttribute> {

    public static final int TOTAL_INTERVAL_MICRO_SEC = 750000; // ms

    @Override
    public void init() {

    }

    @Override
    public int getBotFlag(IpAttribute ipAttribute) {


        int botFlag = NON_BOT_FLAG;
        if (ipAttribute.getSingleClickSessionCount() >= 1) {
           botFlag=SCS_ON_IP;
        }

        return botFlag;
    }

}
