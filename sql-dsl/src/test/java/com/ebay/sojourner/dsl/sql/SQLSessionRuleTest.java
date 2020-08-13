package com.ebay.sojourner.dsl.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import org.junit.Test;

public class SQLSessionRuleTest {

  private RuleDefinition getRuleDef() {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(1L);
    ruleDefinition.setCategory("SESSION");
    ruleDefinition.setContent("SELECT 1 as bot FROM soj.idl_session WHERE agentInfo = 'bot'");
    return ruleDefinition;
  }


  @Test
  public void testRuleExecute() throws Exception {
    SQLSessionRule rule = new SQLSessionRule(getRuleDef());

    UbiSession ubiSession = new UbiSession();
    ubiSession.setAgentInfo("bot");

    Integer result = rule.execute(ubiSession);

    assertEquals(1, result);
  }


}