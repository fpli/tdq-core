package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionDwellMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionDwellMetricsTest {
    private SessionDwellMetrics sessionDwellMetrics;
    private UbiEvent ubiEvent;
    private SessionAccumulator sessionAccumulator;
    private Long[] minMaxEventTimestamp;

    @BeforeEach
    public void setup(){
        sessionAccumulator = new SessionAccumulator();
        ubiEvent = new UbiEvent();
        sessionDwellMetrics = new SessionDwellMetrics();
        minMaxEventTimestamp = new Long[]{Long.MAX_VALUE,Long.MIN_VALUE};
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
    }

    @Test
    public void test_feed_when_iframe_is_Integer_MinValue(){
        ubiEvent.setEventTimestamp(0L);
        ubiEvent.setIframe(Integer.MIN_VALUE);
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(minMaxEventTimestamp[0]);
        assertThat(actual[1]).isEqualTo(minMaxEventTimestamp[1]);
    }

    @Test
    public void test_feed_when_rdt_is_Integer_MinValue(){
        ubiEvent.setEventTimestamp(0L);
        ubiEvent.setRdt(Integer.MIN_VALUE);
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(minMaxEventTimestamp[0]);
        assertThat(actual[1]).isEqualTo(minMaxEventTimestamp[1]);
    }

    @Test
    public void test_feed_when_iframe_not_0(){
        ubiEvent.setEventTimestamp(0L);
        ubiEvent.setIframe(1);
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(minMaxEventTimestamp[0]);
        assertThat(actual[1]).isEqualTo(minMaxEventTimestamp[1]);
    }

    @Test
    public void test_feed_when_rdt_not_0(){
        ubiEvent.setEventTimestamp(0L);
        ubiEvent.setRdt(1);
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(minMaxEventTimestamp[0]);
        assertThat(actual[1]).isEqualTo(minMaxEventTimestamp[1]);
    }

    @Test
    public void test_feed_when_eventTimestamp_less_minMaxEventTimestamp_0(){
        ubiEvent.setEventTimestamp(0L);
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(new Long[]{1L,0L});
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(0L);
        assertThat(actual[1]).isEqualTo(0L);
    }

    @Test
    public void test_feed_when_eventTimestamp_more_minMaxEventTimestamp_1_and_less_minMaxEventTimestamp_0(){
        ubiEvent.setEventTimestamp(1L);
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(new Long[]{2L,0L});
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(1L);
        assertThat(actual[1]).isEqualTo(1L);
    }

    @Test
    public void test_feed_when_eventTimestamp_more_minMaxEventTimestamp_1(){
        ubiEvent.setEventTimestamp(1L);
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(new Long[]{0L,0L});
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(0L);
        assertThat(actual[1]).isEqualTo(1L);
    }

    @Test
    public void test_feed_when_eventTimestamp_less_minMaxEventTimestamp_1_and_more_minMaxEventTimestamp_0(){
        ubiEvent.setEventTimestamp(1L);
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(new Long[]{0L,2L});
        sessionDwellMetrics.feed(ubiEvent,sessionAccumulator);
        Long[] actual = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
        assertThat(actual[0]).isEqualTo(0L);
        assertThat(actual[1]).isEqualTo(2L);
    }
}
