package com.ebay.sojourner.ubd.common.rule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.util.BotHostMatcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class BotRuleForDeclarativeHostTest {

  BotRuleForDeclarativeHost botRuleForDeclarativeHost;
  @Mock
  BotHostMatcher botHostMatcher;

  @BeforeEach
  public void setup() throws Exception {
    initMocks(this);
    botRuleForDeclarativeHost = new BotRuleForDeclarativeHost();
    setInternalState(botRuleForDeclarativeHost, botHostMatcher);
  }

  @Test
  public void test_getBotFlag_hit222() throws Exception {
    String ip = "1.2.3.4";
    IpAttribute ipAttribute = new IpAttribute();
    ipAttribute.setIsAllAgentHoper(true);
    ipAttribute.setTotalCnt(301);
    ipAttribute.setClientIp(ip);
    when(botHostMatcher.isBotIp(ip)).thenReturn(true);

    int botFlag = botRuleForDeclarativeHost.getBotFlag(ipAttribute);
    Assertions.assertThat(botFlag).isEqualTo(222);
    verify(botHostMatcher, times(1)).isBotIp(ip);
  }

  @Test
  public void test_getBotFlag_not_hit222() throws Exception {
    String ip = "1.2.3.4";
    IpAttribute ipAttribute = new IpAttribute();
    ipAttribute.setIsAllAgentHoper(true);
    ipAttribute.setTotalCnt(300);
    ipAttribute.setClientIp(ip);

    int botFlag = botRuleForDeclarativeHost.getBotFlag(ipAttribute);
    Assertions.assertThat(botFlag).isEqualTo(0);
  }
}
