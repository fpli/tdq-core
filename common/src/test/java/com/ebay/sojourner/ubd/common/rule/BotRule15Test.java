package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class BotRule15Test extends BaseRulesTest<UbiSession>{
    private BotRule15 botRule15;
    private List<RulesTestCase> rulesTestCaseList;

    @BeforeEach
    public void setup() throws Exception {
        botRule15 = new BotRule15();
        rulesTestCaseList = loadTestCases("rule15.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() {
        return generateDynamicTests(rulesTestCaseList, botRule15);
    }
}