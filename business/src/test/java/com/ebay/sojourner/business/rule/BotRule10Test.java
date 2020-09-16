package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.UbiSession;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule10Test extends BaseRulesTest<UbiSession> {

  private BotRule10 botRule10;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule10 = new BotRule10();
    rulesTestCaseList = loadTestCases("rule10.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule10);
  }
}
