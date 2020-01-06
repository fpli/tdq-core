package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BotRule7Test {
    private BotRule7 rule7;
    private IpAttribute ipAttribute;

    @BeforeEach
    public void setup(){
        rule7 = new BotRule7();
        ipAttribute = new IpAttribute();
    }

    @Test
    public void test_getBotFlag_hit(){
        ipAttribute.setScsCount(2);
        ipAttribute.setTotalCnt(21);
        int actual = rule7.getBotFlag(ipAttribute);
        assertThat(actual).isEqualTo(7);
    }

    @Test
    public void test_getBotFlag_notHit(){
        ipAttribute.setScsCount(0);
        int actual = rule7.getBotFlag(ipAttribute);
        assertThat(actual).isEqualTo(0);
    }
}
