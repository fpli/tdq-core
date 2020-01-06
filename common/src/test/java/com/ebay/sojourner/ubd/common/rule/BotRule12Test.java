package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BotRule12Test {
    private BotRule12 botRule12;
    private UbiSession ubiSession;
    private Set<Integer> botFlagList;
    private Long[] minMaxEventTimestamp;

    @BeforeEach
    public void setup(){
        botRule12 = new BotRule12();
        ubiSession = new UbiSession();
        botFlagList = ubiSession.getBotFlagList();
    }

    @Test
    public void test_getBotFlag_when_sessionBotFlagList_contains_12(){
        botFlagList.add(12);
        minMaxEventTimestamp = new Long[]{123L,456L};
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(12);
    }

    @Test
    public void test_getBotFlag_when_start_is_0(){
        botFlagList.add(1);
        minMaxEventTimestamp = new Long[]{0L,456L};
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void test_getBotFlag_when_end_is_0(){
        botFlagList.add(1);
        minMaxEventTimestamp = new Long[]{123L,0L};
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void test_getBotFlag_when_enevtCount_less_1(){
        botFlagList.add(1);
        minMaxEventTimestamp = new Long[]{123L,456L};
        ubiSession.setEventCnt(0);
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void test_getBotFlag_when_duration_more_750000(){
        botFlagList.add(1);
        minMaxEventTimestamp = new Long[]{1L,7500000L};
        ubiSession.setEventCnt(2);
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void test_getBotFlag_when_duration_less_750000(){
        botFlagList.add(1);
        minMaxEventTimestamp = new Long[]{1L,700000L};
        ubiSession.setEventCnt(2);
        ubiSession.setBotFlagList(botFlagList);
        ubiSession.setMinMaxEventTimestamp(minMaxEventTimestamp);
        int actual = botRule12.getBotFlag(ubiSession);
        assertThat(actual).isEqualTo(12);
    }
}
