package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class BotRule206Test extends BaseRulesTest<UbiSession> {
    private BotRule206 botRule206;
    private List<RulesTestCase> rulesTestCaseList;

    @BeforeEach
    public void setup() throws Exception {
        botRule206 = new BotRule206();
        rulesTestCaseList = loadTestCases("rule206.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() {
        return generateDynamicTests(rulesTestCaseList, botRule206);
    }
}
