package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule8Test extends BaseRulesTest<AgentIpAttribute> {

  private BotRule8 botRule8;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule8 = new BotRule8();
    rulesTestCaseList = loadTestCases("rule8.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule8);
  }
}
