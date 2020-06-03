package com.ebay.sojourner.business.ubd.rule;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.UbiBotFilter;
import com.ebay.sojourner.common.util.UbiSessionHelper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UbiSessionHelper.class)
public class BotRule11Test {

  @Mock
  UbiBotFilter mockBotFilter;
  private BotRule11 botRule11;
  private UbiSession ubiSession;
  private UbiSessionHelper ubiSessionHelper;

  @Before
  public void setup() throws InterruptedException {
    ubiSession = new UbiSession();
    botRule11 = new BotRule11();
    ubiSessionHelper = new UbiSessionHelper();
    MockitoAnnotations.initMocks(this);
    //    mockStatic(UbiSessionHelper.class);
    Whitebox.setInternalState(botRule11, mockBotFilter);
    PowerMockito.when(mockBotFilter.filter(ubiSession, 11)).thenReturn(false);
  }

  @Ignore
  @Test
  public void testGetBotFlag_SPECIFIC_SPIDER_IAB() throws InterruptedException {
    PowerMockito.when(ubiSessionHelper.isIabAgent(ubiSession)).thenReturn(true);

    int botFlag = botRule11.getBotFlag(ubiSession);

    Mockito.verify(mockBotFilter,
        Mockito.times(1)).filter(ubiSession, 11);
    Assertions.assertThat(botFlag).isEqualTo(11);
  }

  @Ignore
  @Test
  public void testGetBotFlag_NON_BOT_FLAG() throws InterruptedException {
    PowerMockito.when(ubiSessionHelper.isIabAgent(ubiSession)).thenReturn(false);

    int botFlag = botRule11.getBotFlag(ubiSession);

    Mockito.verify(mockBotFilter,
        Mockito.times(1)).filter(ubiSession, 11);
    Assertions.assertThat(botFlag).isEqualTo(0);
  }
}
