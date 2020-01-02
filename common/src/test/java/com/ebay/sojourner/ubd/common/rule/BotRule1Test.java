package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BotRule1Test {

    private BotRule1 rule1;
    private UbiEvent ubiEvent;

    @BeforeEach
    public void setUp() {
        rule1 = new BotRule1();
        ubiEvent = new UbiEvent();
    }

    @Test
    public void test_getBotFlag_hit() {
        ubiEvent.setAgentInfo("I am a bot");
        int actual = rule1.getBotFlag(ubiEvent);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void test_getBotFlag_notHit() {
        //1. prepare mock data
        ubiEvent.setAgentInfo("Chrome");

        //2. invoke
        int actual = rule1.getBotFlag(ubiEvent);

        //3. assertion
        assertThat(actual).isEqualTo(0);
    }
}