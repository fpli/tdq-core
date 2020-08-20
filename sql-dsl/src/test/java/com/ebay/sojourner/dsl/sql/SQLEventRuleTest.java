package com.ebay.sojourner.dsl.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import org.junit.Test;

public class SQLEventRuleTest {

  private RuleDefinition getRuleDef(String sql) {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(1L);
    ruleDefinition.setCategory("EVENT");
    ruleDefinition.setContent(sql);
    return ruleDefinition;
  }

  @Test
  public void testRuleExecute() throws Exception {
    String sql = "SELECT 1 as bot FROM soj.idl_event WHERE agentInfo = 'bot'";
    SQLEventRule rule = new SQLEventRule(getRuleDef(sql));

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setAgentInfo("bot");

    Integer result = rule.execute(ubiEvent);

    assertEquals(1, result);
  }


  @Test
  public void testIcfAllZero() {
    long icfBinary = 0b0000000000000000;
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setIcfBinary(icfBinary);

    assertEquals(0, Rules.ICF_RULE_1.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_2.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_3.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_4.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_5.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_6.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_7.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_10.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_11.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_12.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_13.execute(ubiEvent));
    assertEquals(0, Rules.ICF_RULE_56.execute(ubiEvent));
  }

  @Test
  public void testIcfAllOne() {
    long icfBinary = 0b0001111111111111 | (1 << 55);
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setIcfBinary(icfBinary);

    assertEquals(801, Rules.ICF_RULE_1.execute(ubiEvent));
    assertEquals(802, Rules.ICF_RULE_2.execute(ubiEvent));
    assertEquals(803, Rules.ICF_RULE_3.execute(ubiEvent));
    assertEquals(804, Rules.ICF_RULE_4.execute(ubiEvent));
    assertEquals(805, Rules.ICF_RULE_5.execute(ubiEvent));
    assertEquals(806, Rules.ICF_RULE_6.execute(ubiEvent));
    assertEquals(807, Rules.ICF_RULE_7.execute(ubiEvent));
    assertEquals(808, Rules.ICF_RULE_10.execute(ubiEvent));
    assertEquals(809, Rules.ICF_RULE_11.execute(ubiEvent));
    assertEquals(810, Rules.ICF_RULE_12.execute(ubiEvent));
    assertEquals(811, Rules.ICF_RULE_13.execute(ubiEvent));
    assertEquals(812, Rules.ICF_RULE_56.execute(ubiEvent));
  }

//  @Test
//  public void testIcfSomeOne() {
//    try {
//      long icfBinary = 0b0001001000000000; // 0x1200
//      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary)
//          .build();
//      assertEquals(0, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(808, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(811, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
//    } catch (Exception e) {
//      fail();
//    }
//  }
}
