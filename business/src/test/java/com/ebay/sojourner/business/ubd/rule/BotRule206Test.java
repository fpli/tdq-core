package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BotRule206Test extends BaseRulesTest<UbiSession> {

  private BotRule206 botRule206;
  private List<RulesTestCase> rulesTestCaseList;

  @BeforeEach
  public void setup() throws Exception {
    botRule206 = new BotRule206();
    rulesTestCaseList = loadTestCases("rule206.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() {
    return generateDynamicTests(rulesTestCaseList, botRule206);
  }
}
