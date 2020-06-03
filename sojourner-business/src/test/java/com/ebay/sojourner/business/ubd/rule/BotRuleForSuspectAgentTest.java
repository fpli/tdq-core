package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRuleForSuspectAgentTest extends BaseRulesTest<AgentAttribute> {

  private BotRuleForSuspectAgent botRuleForSuspectAgent;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRuleForSuspectAgent = new BotRuleForSuspectAgent();
    rulesTestCaseList = loadTestCases("BotRuleForSuspectAgentTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRuleForSuspectAgent);
  }
}
