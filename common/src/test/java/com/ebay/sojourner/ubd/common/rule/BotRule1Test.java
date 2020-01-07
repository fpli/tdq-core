package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.RuleConstants;
import com.ebay.sojourner.ubd.common.util.RuleInputUtils;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class BotRule1Test {
    private static BotRule1 botRule1 = null;
    private static HashMap<String, Object> map = null;
    private static String caseItem = null;
    private static String botRule = null;

    @BeforeEach
    public void setUp() {
        botRule1 = new BotRule1();
        map = YamlUtil.getInstance().loadFileMap(RuleConstants.rule1Path);
        botRule = RuleConstants.Rule1;
    }

    @Test
    public void test_getBotFlag_when_agentInfo_like_Chrome(){
        caseItem = RuleConstants.CASE1;
        HashMap<Object, Integer> eventRuleInputAndExpect = RuleInputUtils.getEventRuleInputAndExpect(map, caseItem, botRule);
        for(Map.Entry<Object,Integer> entry : eventRuleInputAndExpect.entrySet()){
            int actual = botRule1.getBotFlag((UbiEvent)entry.getKey());
            assertThat(actual).isEqualTo(entry.getValue());
        }
    }

    @Test
    public void test_getBotFlag_when_agentInfo_like_bot(){
        caseItem = RuleConstants.CASE2;
        HashMap<Object, Integer> eventRuleInputAndExpect = RuleInputUtils.getEventRuleInputAndExpect(map, caseItem, botRule);
        for(Map.Entry<Object,Integer> entry : eventRuleInputAndExpect.entrySet()){
            int actual = botRule1.getBotFlag((UbiEvent)entry.getKey());
            assertThat(actual).isEqualTo(entry.getValue());
        }
    }
}