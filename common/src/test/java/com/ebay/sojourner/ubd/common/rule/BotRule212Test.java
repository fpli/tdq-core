package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class BotRule212Test extends BaseRulesTest<UbiSession> {
    private BotRule212 botRule212;
    private List<RulesTestCase> rulesTestCaseList;

    @BeforeEach
    public void setup() throws Exception {
        botRule212 = new BotRule212();
        rulesTestCaseList = loadTestCases("rule212.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() {
        return generateDynamicTests(rulesTestCaseList, botRule212);
    }
}
