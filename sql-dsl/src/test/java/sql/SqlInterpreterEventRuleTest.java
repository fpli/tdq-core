package sql;

import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_10_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_11_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_12_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_13_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_1_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_2_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_3_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_4_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_56_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_5_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_6_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.ICF_RULE_7_INTERPRETER;
import static com.ebay.sojourner.dsl.sql.Rules.RULE_1_INTERPRETER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.dsl.sql.SqlEventRule;
import org.junit.Test;

public class SqlInterpreterEventRuleTest {

  @Test
  public void testRegex() {
    try {
      SqlEventRule rule = RULE_1_INTERPRETER;
      assertEquals(1, rule.getBotFlag(new UbiEventBuilder().agentInfo("googlebot").build()));
      assertEquals(1, rule.getBotFlag(new UbiEventBuilder().agentInfo("crawler").build()));
      assertEquals(0, rule.getBotFlag(new UbiEventBuilder().agentInfo("chrome").build()));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testInterpreterUdf() {
    try {
      assertEquals(4, TestUtils.UDF_1_INTERPRETER.getBotFlag(new UbiEventBuilder().build()));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfAllZero() {
    try {
      long icfBinary = 0b0000000000000000;
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_10_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_13_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_56_INTERPRETER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfAllOne() {
    try {
      long icfBinary = 0b0001111111111111 | (1 << 55);
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(801, ICF_RULE_1_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(802, ICF_RULE_2_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(803, ICF_RULE_3_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(804, ICF_RULE_4_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(805, ICF_RULE_5_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(806, ICF_RULE_6_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(807, ICF_RULE_7_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(808, ICF_RULE_10_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(809, ICF_RULE_11_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(810, ICF_RULE_12_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(811, ICF_RULE_13_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(812, ICF_RULE_56_INTERPRETER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testIcfSomeOne() {
    try {
      long icfBinary = 0b0001001000000000; // 0x1200
      UbiEvent ubiEvent = new UbiEventBuilder().icfBinary(icfBinary).build();
      assertEquals(0, ICF_RULE_1_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_2_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_3_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_4_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_5_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_6_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_7_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(808, ICF_RULE_10_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_11_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_12_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(811, ICF_RULE_13_INTERPRETER.getBotFlag(ubiEvent));
      assertEquals(0, ICF_RULE_56_INTERPRETER.getBotFlag(ubiEvent));
    } catch (Exception e) {
      fail();
    }
  }
}
