package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.PageCntMetrics;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

public class PageCntMetricsTest {

    private PageCntMetrics pageCntMetrics;
    private UbiSession ubiSession;

    @Mock PageIndicator mockPageIndicator;
    @Mock UbiEvent ubiEvent;
    @Mock SessionAccumulator sessionAccumulator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        pageCntMetrics = new PageCntMetrics();
        setInternalState(pageCntMetrics, mockPageIndicator);

        when(mockPageIndicator.isCorrespondingPageEvent(ubiEvent)).thenReturn(true);
        ubiSession = new UbiSession();
    }

    @Test
    public void feed() {
        ubiSession.setPageCnt(100);
        when(sessionAccumulator.getUbiSession()).thenReturn(ubiSession);

        pageCntMetrics.feed(ubiEvent, sessionAccumulator);

        Assertions.assertThat(ubiSession.getPageCnt()).isEqualTo(101);
        verify(mockPageIndicator, times(1)).isCorrespondingPageEvent(ubiEvent);
    }
}