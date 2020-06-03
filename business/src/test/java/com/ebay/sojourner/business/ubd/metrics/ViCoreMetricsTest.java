package com.ebay.sojourner.business.ubd.metrics;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import com.ebay.sojourner.business.ubd.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.FlagUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FlagUtils.class)
public class ViCoreMetricsTest {

  @Mock
  PageIndicator mockPageIndicator;
  @Mock
  UbiEvent ubiEvent;
  @Mock
  SessionAccumulator sessionAccumulator;
  private ViCoreMetrics viCoreMetrics;
  private UbiSession ubiSession;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    viCoreMetrics = new ViCoreMetrics();
    setInternalState(viCoreMetrics, mockPageIndicator);

    ubiEvent.setItemId(1L);
    ubiEvent.setRdt(true);
    ubiEvent.setIframe(true);

    mockStatic(FlagUtils.class);
    when(FlagUtils.matchFlag(eq(ubiEvent), anyInt(), anyInt())).thenReturn(true);
    when(mockPageIndicator.isCorrespondingPageEvent(ubiEvent)).thenReturn(true);

    ubiSession = new UbiSession();
  }

  @Test
  public void test_feed() throws Exception {
    ubiSession.setViCoreCnt(100);
    when(sessionAccumulator.getUbiSession()).thenReturn(ubiSession);

    viCoreMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertThat(ubiSession.getViCoreCnt()).isEqualTo(101);
    verify(mockPageIndicator, times(1)).isCorrespondingPageEvent(ubiEvent);
  }
}
