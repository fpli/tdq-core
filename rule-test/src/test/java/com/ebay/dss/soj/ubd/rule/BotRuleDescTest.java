package com.ebay.dss.soj.ubd.rule;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BotRuleDescTest {

  BotRuleDesc botRuleDesc;

  @BeforeEach
  public void init() {
    String sql = "select 1 as bot_flag from ubi_event";
    botRuleDesc = BotRuleDesc.of(sql);
  }

  @Test
  public void testToBotRuleDesc() {
    Assertions.assertEquals(new BotRuleDesc(1, "ubi_event", Duration.ofMinutes(10)), botRuleDesc);
  }

  @Test
  public void testGenBeforeScript() {
    String script = BotRuleDesc.genBeforeCheckScript(botRuleDesc);
    String expected = "select * from default.ubi_session_dq_overall";
    Assertions.assertEquals(expected, script);
  }

  @Test
  public void testGenAfterScript() {
    String script = BotRuleDesc.genAfterCheckScript(botRuleDesc);
    String expected = "select * from default.ubi_bot_dq_overall";
    Assertions.assertEquals(expected, script);
  }
}
