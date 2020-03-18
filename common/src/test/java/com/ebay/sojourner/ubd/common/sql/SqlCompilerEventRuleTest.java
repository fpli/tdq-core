package com.ebay.sojourner.ubd.common.sql;

import static com.ebay.sojourner.ubd.common.sql.Rules.*;
import static com.ebay.sojourner.ubd.common.sql.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.junit.Test;

public class SqlCompilerEventRuleTest {

  @Test
  public void testRegex() {
    try {
      SqlEventRule rule = RULE_1_COMPILER;
      assertEquals(1, rule.getBotFlag(new UbiEventBuilder().agentInfo("googlebot").build()));
      assertEquals(1, rule.getBotFlag(new UbiEventBuilder().agentInfo("crawler").build()));
      assertEquals(0, rule.getBotFlag(new UbiEventBuilder().agentInfo("chrome").build()));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUdf() {
    try {
      assertEquals(4, UDF_1_COMPILER.getBotFlag(new UbiEventBuilder().build()));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test
  public void testIcfAllZero() {
    try {
      long icfBinary = 0b0000000000000000;
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfAllOne() {
    try {
      long icfBinary = 0b0001111111111111 | (1 << 55);
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(801, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
      assertEquals(802, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
      assertEquals(803, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
      assertEquals(804, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
      assertEquals(805, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
      assertEquals(806, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
      assertEquals(807, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
      assertEquals(808, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
      assertEquals(809, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
      assertEquals(810, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
      assertEquals(811, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
      assertEquals(812, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfSomeOne() {
    try {
      long icfBinary = 0b0001001000000000; // 0x1200
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7_COMPILER.getBotFlag(ubiEvent));
      assertEquals(808, ICF_RULE_10_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12_COMPILER.getBotFlag(ubiEvent));
      assertEquals(811, ICF_RULE_13_COMPILER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_56_COMPILER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }
}
