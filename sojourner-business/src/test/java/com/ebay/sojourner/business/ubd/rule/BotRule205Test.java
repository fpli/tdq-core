package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule205;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule205Test extends BaseRulesTest<UbiSession> {

  private BotRule205 botRule205;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule205 = new BotRule205();
    rulesTestCaseList = loadTestCases("rule205.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule205);
  }
}
