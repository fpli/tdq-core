package com.ebay.sojourner.dsl.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import org.junit.Test;

public class SQLEventRuleTest {

  private RuleDefinition getRuleDef() {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(1L);
    ruleDefinition.setCategory("EVENT");
    ruleDefinition.setContent("SELECT 1 as bot FROM soj.idl_event WHERE agentInfo = 'bot'");
    return ruleDefinition;
  }

  @Test
  public void testRuleExecute() throws Exception {
    SQLEventRule rule = new SQLEventRule(getRuleDef());

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setAgentInfo("bot");

    Integer result = rule.execute(ubiEvent);

    assertEquals(1, result);
  }

//  @Test
//  public void testUdf() {
//    try {
//      assertEquals(4, TestUtils.UDF_1_COMPILER.getBotFlag(new UbiEventBuilder().build()));
//    } catch (Exception e) {
//      System.out.println(e);
//    }
//  }
//
//  @Test
//  public void testIcfAllZero() {
//    try {
//      long icfBinary = 0b0000000000000000;
//      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
//      assertEquals(0, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(0, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
//    } catch (Exception e) {
//      fail();
//    }
//  }
//
//  @Test
//  public void testIcfAllOne() {
//    try {
//      long icfBinary = 0b0001111111111111 | (1 << 55);
//      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
//      assertEquals(801, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(802, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(803, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(804, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(805, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(806, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(807, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(808, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(809, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(810, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(811, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
//      assertEquals(812, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
//    } catch (Exception e) {
//      fail();
//    }
//  }
//
//  @Test
//  public void testIcfSomeOne() {
//    try {
//      long icfBinary = 0b0001001000000000; // 0x1200
//      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
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
