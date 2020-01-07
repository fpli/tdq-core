package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiEvent;

import java.util.HashMap;

public class InitEventRuleInput {
    private static UbiEvent ubiEvent = null;

    public static UbiEvent init(HashMap map,String botRule){
        if (botRule != null && botRule.contains("Rule1")) {
            ubiEvent = new UbiEvent();
            ubiEvent.setAgentInfo(TypeTransUtil.ObjectToString(map.get(RuleConstants.AGENTINFO)));
        }
        return ubiEvent;
    }
}
