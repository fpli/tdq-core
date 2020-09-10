package com.ebay.sojourner.business.ubd.detectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.business.ubd.rule.BotRule206;
import com.ebay.sojourner.business.ubd.rule.BotRule208;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.model.rule.Rule;
import com.ebay.sojourner.common.util.UbiBotFilter;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionEndBotDetector.class)
public class SessionEndBotDetectorTest {

  SessionEndBotDetector sessionEndBotDetector;

  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  BotRule206 mockRule206 = mock(BotRule206.class);
  BotRule208 mockRule208 = mock(BotRule208.class);

  @Before
  public void setUp() throws Exception {
    PowerMockito.whenNew(UbiBotFilter.class).withNoArguments().thenReturn(mockBotFilter);
    PowerMockito.whenNew(BotRule206.class).withNoArguments().thenReturn(mockRule206);
    PowerMockito.whenNew(BotRule208.class).withNoArguments().thenReturn(mockRule208);
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    when(mockRule206.getBotFlag(any())).thenReturn(206);
    when(mockRule208.getBotFlag(any())).thenReturn(0);

    sessionEndBotDetector = SessionEndBotDetector.getInstance();
  }

  @Test
  public void getBotFlagList() throws Exception {
    UbiSession ubiSession = new UbiSession();

    Set<Integer> result = sessionEndBotDetector.getBotFlagList(ubiSession);
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.contains(206)).isTrue();
  }

  @Test
  public void initBotRules() {
    Set<Rule> botRules = Whitebox.getInternalState(sessionEndBotDetector, "botRules", SessionEndBotDetector.class);
    assertThat(botRules.size()).isEqualTo(2);
  }
}