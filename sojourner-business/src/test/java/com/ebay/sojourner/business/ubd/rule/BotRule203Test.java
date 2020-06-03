package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule203;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule203Test extends BaseRulesTest<UbiSession> {

  private BotRule203 botRule203;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule203 = new BotRule203();
    rulesTestCaseList = loadTestCases("rule203.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule203);
  }
}
