package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule12;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule12Test extends BaseRulesTest<UbiSession> {

  private BotRule12 botRule12;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule12 = new BotRule12();
    rulesTestCaseList = loadTestCases("rule12.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule12);
  }
}
