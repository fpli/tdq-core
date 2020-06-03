package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule5Test extends BaseRulesTest<AgentIpAttribute> {

  private BotRule5 botRule5;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule5 = new BotRule5();
    rulesTestCaseList = loadTestCases("rule5.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule5);
  }
}
