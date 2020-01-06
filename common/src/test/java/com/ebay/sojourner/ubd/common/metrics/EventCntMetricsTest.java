package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.EventCntMetrics;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EventCntMetricsTest {
    private EventCntMetrics eventCntMetrics;
    private UbiEvent ubiEvent;
    private SessionAccumulator sessionAccumulator;
    private PageIndicator indicator;
    private static UBIConfig ubiConfig;

    @BeforeEach
    public void setup() throws Exception{
        eventCntMetrics = new EventCntMetrics();
        eventCntMetrics.init();
        ubiEvent = new UbiEvent();
        sessionAccumulator = new SessionAccumulator();
        sessionAccumulator.getUbiSession().setAbsEventCnt(0);
        sessionAccumulator.getUbiSession().setEventCnt(0);
        sessionAccumulator.getUbiSession().setNonIframeRdtEventCnt(0);
    }

    @Test
    public void test_feed_when_iframe_not_0(){
        ubiEvent.setIframe(1);
        eventCntMetrics.feed(ubiEvent,sessionAccumulator);
        int absEventCnt = sessionAccumulator.getUbiSession().getAbsEventCnt();
        int eventCnt = sessionAccumulator.getUbiSession().getEventCnt();
        int nonIframeRdtEventCnt = sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt();
        assertThat(absEventCnt).isEqualTo(1);
        assertThat(eventCnt).isEqualTo(0);
        assertThat(nonIframeRdtEventCnt).isEqualTo(0);
    }

    @Test
    public void test_feed_when_rdt_not_0_and_pageIds_not_contains_pageId(){
        ubiEvent.setRdt(1);
        ubiEvent.setPageId(0);
        eventCntMetrics.feed(ubiEvent,sessionAccumulator);
        int absEventCnt = sessionAccumulator.getUbiSession().getAbsEventCnt();
        int eventCnt = sessionAccumulator.getUbiSession().getEventCnt();
        int nonIframeRdtEventCnt = sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt();
        assertThat(absEventCnt).isEqualTo(1);
        assertThat(eventCnt).isEqualTo(0);
        assertThat(nonIframeRdtEventCnt).isEqualTo(0);
    }

    @Test
    public void test_feed_when_rdt_not_0_and_pageIds_contains_pageId(){
        ubiEvent.setRdt(1);
        ubiEvent.setPageId(4018);
        eventCntMetrics.feed(ubiEvent,sessionAccumulator);
        int absEventCnt = sessionAccumulator.getUbiSession().getAbsEventCnt();
        int eventCnt = sessionAccumulator.getUbiSession().getEventCnt();
        int nonIframeRdtEventCnt = sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt();
        assertThat(absEventCnt).isEqualTo(1);
        assertThat(eventCnt).isEqualTo(1);
        assertThat(nonIframeRdtEventCnt).isEqualTo(0);
    }

    @Test
    public void test_feed_when_rdt_is_0(){
        ubiEvent.setRdt(0);
        eventCntMetrics.feed(ubiEvent,sessionAccumulator);
        int absEventCnt = sessionAccumulator.getUbiSession().getAbsEventCnt();
        int eventCnt = sessionAccumulator.getUbiSession().getEventCnt();
        int nonIframeRdtEventCnt = sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt();
        assertThat(absEventCnt).isEqualTo(1);
        assertThat(eventCnt).isEqualTo(1);
        assertThat(nonIframeRdtEventCnt).isEqualTo(1);
    }
}
