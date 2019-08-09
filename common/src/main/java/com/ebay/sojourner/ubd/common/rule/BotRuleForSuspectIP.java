package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;

public class BotRuleForSuspectIP implements Rule<IpAttribute> {
    public static final int MAX_CGUID_THRESHOLD = 5;
    public static final int SESSION_COUNT_THRESHOLD = 300;

    @Override
    public int getBotFlag(IpAttribute ipAttribute) {
        if (ipAttribute.getIsAllAgentHoper() && ipAttribute.getTotalCnt() > SESSION_COUNT_THRESHOLD) {
            return 223;
        }
        if (ipAttribute.getTotalCntForSec1() > SESSION_COUNT_THRESHOLD) {
            return 223;
        }
        return 0;
    }

    @Override
    public void init() {

    }

}
