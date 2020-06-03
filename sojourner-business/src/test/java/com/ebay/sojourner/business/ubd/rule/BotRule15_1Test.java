package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.GuidAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule15_Cross;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule15_1Test extends BaseRulesTest<GuidAttribute> {

  private BotRule15_Cross botRule15_1;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule15_1 = new BotRule15_Cross();
    rulesTestCaseList = loadTestCases("rule15_1.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule15_1);
  }
}
