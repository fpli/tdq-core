package com.ebay.sojourner.ubd.common.rule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UbiSessionHelper.class)
public class BotRule11Test {

  @Mock UbiBotFilter mockBotFilter;
  private BotRule11 botRule11;
  private UbiSession ubiSession;

  @Before
  public void setup() {
    ubiSession = new UbiSession();
    botRule11 = new BotRule11();
    initMocks(this);
    mockStatic(UbiSessionHelper.class);
    setInternalState(botRule11, mockBotFilter);
    when(mockBotFilter.filter(ubiSession, 11)).thenReturn(false);
  }

  @Test
  public void test_getBotFlag_SPECIFIC_SPIDER_IAB() {
    when(UbiSessionHelper.isIabAgent(ubiSession)).thenReturn(true);

    int botFlag = botRule11.getBotFlag(ubiSession);

    verify(mockBotFilter, times(1)).filter(ubiSession, 11);
    Assertions.assertThat(botFlag).isEqualTo(11);
  }

  @Test
  public void test_getBotFlag_NON_BOT_FLAG() {
    when(UbiSessionHelper.isIabAgent(ubiSession)).thenReturn(false);

    int botFlag = botRule11.getBotFlag(ubiSession);

    verify(mockBotFilter, times(1)).filter(ubiSession, 11);
    Assertions.assertThat(botFlag).isEqualTo(0);
  }
}
