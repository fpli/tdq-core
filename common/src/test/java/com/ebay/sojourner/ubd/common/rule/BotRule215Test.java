package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class BotRule215Test extends BaseRulesTest<UbiSession> {
    private BotRule215 botRule215;
    private List<RulesTestCase> rulesTestCaseList;

    @BeforeEach
    public void setup() throws Exception {
        botRule215 = new BotRule215();
        rulesTestCaseList = loadTestCases("rule215.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() {
        return generateDynamicTests(rulesTestCaseList, botRule215);
    }
}
