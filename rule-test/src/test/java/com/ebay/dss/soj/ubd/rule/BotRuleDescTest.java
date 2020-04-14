package com.ebay.dss.soj.ubd.rule;

import java.time.Duration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BotRuleDescTest {

  BotRuleDesc botRuleDesc;

  @Before
  public void init() {
    String sql = "select 1 as bot_flag from ubi_event";
    botRuleDesc = BotRuleDesc.of(sql);
  }

  @Test
  public void testToBotRuleDesc() {
    Assert.assertEquals(new BotRuleDesc(1, "ubi_event", Duration.ofMinutes(10)), botRuleDesc);
  }

  @Test
  public void testGenBeforeScript() {
    String script = BotRuleDesc.genBeforeCheckScript(botRuleDesc);
    String expected = "select * from default.ubi_session_dq_overall";
    Assert.assertEquals(expected, script);
  }

  @Test
  public void testGenAfterScript() {
    String script = BotRuleDesc.genAfterCheckScript(botRuleDesc);
    String expected = "select * from default.ubi_bot_dq_overall";
    Assert.assertEquals(expected, script);
  }
}
