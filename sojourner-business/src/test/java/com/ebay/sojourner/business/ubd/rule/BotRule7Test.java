package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule7Test extends BaseRulesTest<IpAttribute> {

  private BotRule7 botRule7;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule7 = new BotRule7();
    rulesTestCaseList = loadTestCases("rule7.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule7);
  }
}
