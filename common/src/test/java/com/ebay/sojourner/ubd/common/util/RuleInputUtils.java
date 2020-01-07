package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

import java.util.HashMap;

public class RuleInputUtils {
    private static HashMap<Object, Integer> hashMap = null;
    private static Integer expectResult;

    public static HashMap<Object,Integer> getEventRuleInputAndExpect(HashMap map,String caseItem,String rule){
        HashMap<String, Object> ruleMap = TypeTransUtil.ObjectToHashMap(map.get(caseItem));
        String inputRuleType = TypeTransUtil.ObjectToString(ruleMap.get("Type"));
        expectResult = TypeTransUtil.ObjectToInteger(ruleMap.get(RuleConstants.EXPECT));
        HashMap<String, Object> inputKeyValuePair = TypeTransUtil.ObjectToHashMap(ruleMap.get(inputRuleType));

        if (inputRuleType != null && inputRuleType.equals(RuleConstants.eventRuleType)) {
            hashMap = new HashMap<>();
            UbiEvent ubiEvent = InitEventRuleInput.init(inputKeyValuePair, rule);
            hashMap.put(ubiEvent,expectResult);

        } else if (inputRuleType != null && inputRuleType.equals(RuleConstants.sessionRuleType)) {
            hashMap = new HashMap<>();
            UbiSession ubiSession = InitSessionRuleInput.init(map, rule);
            hashMap.put(ubiSession,expectResult);
        } else if (inputRuleType != null && inputRuleType.equals(RuleConstants.ipAttributeRuleType)) {

        } else if (inputRuleType != null && inputRuleType.equals(RuleConstants.agentAttributeRuleType)) {

        } else if (inputRuleType != null && inputRuleType.equals(RuleConstants.agentIpAttributeRuleType)) {

        }

        return hashMap;
    }
}
