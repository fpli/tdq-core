package com.ebay.sojourner.ubd.common.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class IcfRuleUtilTest {

    @Test
    public void testIcfRuleUtil_10() {
        String checkString = "1200";
        long checkLong = NumberUtils.hexToDec(checkString);
        Assert.assertEquals(IcfRuleUtils.getIcfRuleType(checkLong, 10),410);
    }

    @Test
    public void testIcfRuleUtil_13() {
        String checkString = "1200";
        long checkLong = NumberUtils.hexToDec(checkString);
        Assert.assertEquals(IcfRuleUtils.getIcfRuleType(checkLong, 13),413);
    }

    @Test
    public void testIcfRuleUtil_0() {
        String checkString = "1200";
        long checkLong = NumberUtils.hexToDec(checkString);
        Assert.assertEquals(IcfRuleUtils.getIcfRuleType(checkLong, 3),0);
    }
}
