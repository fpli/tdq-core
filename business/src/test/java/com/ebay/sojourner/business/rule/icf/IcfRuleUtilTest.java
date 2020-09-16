package com.ebay.sojourner.business.rule.icf;

import com.ebay.sojourner.common.util.IcfRuleUtils;
import com.ebay.sojourner.common.util.NumberUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class IcfRuleUtilTest {

  @Test
  public void testIcfRuleUtil_10() {
    String checkString = "1200";
    long checkLong = NumberUtils.hexToDec(checkString);
    Assert.assertEquals(808, IcfRuleUtils.getIcfRuleType(checkLong, 10));
  }

  @Test
  public void testIcfRuleUtil_13() {
    String checkString = "1200";
    long checkLong = NumberUtils.hexToDec(checkString);
    Assert.assertEquals(811, IcfRuleUtils.getIcfRuleType(checkLong, 13));
  }

  @Test
  public void testIcfRuleUtil_0() {
    String checkString = "1200";
    long checkLong = NumberUtils.hexToDec(checkString);
    Assert.assertEquals(0, IcfRuleUtils.getIcfRuleType(checkLong, 3));
  }
}
