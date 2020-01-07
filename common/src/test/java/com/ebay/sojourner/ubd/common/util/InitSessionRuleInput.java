package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;

import java.util.HashMap;

public class InitSessionRuleInput {
    private static UbiSession ubiSession = null;

    public static UbiSession init(HashMap map, String botRule){
        if (botRule != null && botRule.contains("Rule12")) {
            ubiSession = new UbiSession();
        }
        return ubiSession;
    }
}
