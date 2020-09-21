package com.ebay.sojourner.dsl.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import org.junit.Test;

public class SQLSessionRuleTest {

  private RuleDefinition getRuleDef(String sql) {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(1L);
    ruleDefinition.setCategory("SESSION");
    ruleDefinition.setContent(sql);
    return ruleDefinition;
  }


  @Test
  public void testRegex() throws Exception {
    String sql = "SELECT 1 as bot FROM soj.idl_session " +
        "WHERE lower(agentInfo) SIMILAR TO '.*bot[^a-z0-9\\_-].*|.*bot|.*spider.*|.*crawl.*|.*ktxn.*' ESCAPE '\\'";
    SQLSessionRule rule = new SQLSessionRule(getRuleDef(sql));

    UbiSession ubiSession = new UbiSession();
    ubiSession.setAgentInfo("Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)");

    Integer result = rule.execute(ubiSession);

    assertEquals(1, result);
  }

}