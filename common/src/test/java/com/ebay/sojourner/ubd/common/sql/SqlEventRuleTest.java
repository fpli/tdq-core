package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.junit.Test;

import static com.ebay.sojourner.ubd.common.sql.Rules.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SqlEventRuleTest {

  @Test
  public void testRegex() {
    try {
      SqlEventRule rule = RULE_1;
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
      SqlEventRule rule = new SqlEventRule(
          "SELECT \"square\"(2) " +
              "FROM \"soj\".\"ubiEvents\""
      );
      assertEquals(4, rule.getBotFlag(new UbiEventBuilder().build()));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfAllZero() {
    try {
      long icfBinary = 0b0000000000000000;
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_8.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_9.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_10.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_13.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_56.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfAllOne() {
    try {
      long icfBinary = 0b0001111111111111 | (1 << 55);
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(401, ICF_RULE_1.getBotFlag(ubiEvent));
      assertEquals(402, ICF_RULE_2.getBotFlag(ubiEvent));
      assertEquals(403, ICF_RULE_3.getBotFlag(ubiEvent));
      assertEquals(404, ICF_RULE_4.getBotFlag(ubiEvent));
      assertEquals(405, ICF_RULE_5.getBotFlag(ubiEvent));
      assertEquals(406, ICF_RULE_6.getBotFlag(ubiEvent));
      assertEquals(407, ICF_RULE_7.getBotFlag(ubiEvent));
      assertEquals(408, ICF_RULE_8.getBotFlag(ubiEvent));
      assertEquals(409, ICF_RULE_9.getBotFlag(ubiEvent));
      assertEquals(410, ICF_RULE_10.getBotFlag(ubiEvent));
      assertEquals(411, ICF_RULE_11.getBotFlag(ubiEvent));
      assertEquals(412, ICF_RULE_12.getBotFlag(ubiEvent));
      assertEquals(413, ICF_RULE_13.getBotFlag(ubiEvent));
      assertEquals(456, ICF_RULE_56.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfSomeOne() {
    try {
      long icfBinary = 0b0001001000000000; // 0x1200
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_8.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_9.getBotFlag(ubiEvent));
      assertEquals(410, ICF_RULE_10.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12.getBotFlag(ubiEvent));
      assertEquals(413, ICF_RULE_13.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }
}
