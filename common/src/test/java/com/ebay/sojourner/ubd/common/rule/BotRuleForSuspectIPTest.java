package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class BotRuleForSuspectIPTest extends BaseRulesTest<IpAttribute> {
    private BotRuleForSuspectIP botRuleForSuspectIP;
    private List<RulesTestCase> rulesTestCaseList;

    @BeforeEach
    public void setup() throws Exception {
        botRuleForSuspectIP = new BotRuleForSuspectIP();
        rulesTestCaseList = loadTestCases("BotRuleForSuspectIPTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() {
        return generateDynamicTests(rulesTestCaseList, botRuleForSuspectIP);
    }
}
