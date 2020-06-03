package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule1Test extends BaseRulesTest<UbiEvent> {

  private BotRule1 botRule1;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule1 = new BotRule1();
    rulesTestCaseList = loadTestCases("rule1.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    //    return generateDynamicTests(rulesTestCaseList, botRule1);
    return null;
  }
}
