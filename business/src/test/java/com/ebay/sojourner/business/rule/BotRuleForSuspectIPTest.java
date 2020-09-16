package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.IpAttribute;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRuleForSuspectIPTest extends BaseRulesTest<IpAttribute> {

  private BotRuleForSuspectIP botRuleForSuspectIP;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRuleForSuspectIP = new BotRuleForSuspectIP();
    rulesTestCaseList = loadTestCases("BotRuleForSuspectIPTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRuleForSuspectIP);
  }
}
