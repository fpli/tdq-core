package com.ebay.dss.soj.ubd.rule;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class BotRuleDescTest {

  BotRuleDesc botRuleDesc;

  @BeforeEach
  public void init() {
    String sql = "select 1 as bot_flag from soj.idl_event";
    botRuleDesc = BotRuleDesc.of(sql);
  }

  @Test
  public void testToBotRuleDesc() {
    Assertions
        .assertEquals(new BotRuleDesc(1, "DEFAULT.UBI_EVENT", Duration.ofMinutes(60)), botRuleDesc);
  }

  @Disabled
  @Test
  public void testGenScript() {
    String script = BotRuleDesc.genCheckScript(botRuleDesc);
    System.out.println(script);
    String expected = "select count(*) from DEFAULT.UBI_EVENT\n"
        + "where array_contains(botFlags, 1)\n"
        + "and sojlib.ts_mils(eventTimestamp) > '2020/05/06 20:00:28.232'\n"
        + "union all\n"
        + "select count(*) from DEFAULT.UBI_EVENT\n"
        + "where array_contains(botFlags, 1001)\n"
        + "and sojlib.ts_mils(eventTimestamp) > '2020/05/06 20:00:28.232'\n";
    Assertions.assertEquals(expected, script);
  }
}
