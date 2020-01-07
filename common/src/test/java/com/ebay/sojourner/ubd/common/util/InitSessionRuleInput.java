package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;

import java.util.HashMap;
import java.util.Set;

public class InitSessionRuleInput {
    private static UbiSession ubiSession = null;
    private static Set<Integer> botFlagList;

    public static UbiSession init(HashMap map, String botRule){
        if (botRule != null && botRule.contains("Rule12")) {
            ubiSession = new UbiSession();
            botFlagList = ubiSession.getBotFlagList();
            botFlagList.add(TypeTransUtil.ObjectToInteger(map.get(RuleConstants.SESSIONFLAG)));
            ubiSession.setMinMaxEventTimestamp((Long[]) map.get(RuleConstants.MINMAXTIMESTAMP));
            ubiSession.setEventCnt(TypeTransUtil.ObjectToInteger(map.get(RuleConstants.EVENTCNT)));
            ubiSession.setBotFlagList(botFlagList);
        }
        return ubiSession;
    }
}
