package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.util.BotHostMatcher;
import com.ebay.sojourner.common.util.TransformUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class BotRuleForDeclarativeHostTest {

  BotRuleForDeclarativeHost botRuleForDeclarativeHost;
  @Mock
  BotHostMatcher botHostMatcher;

  @BeforeEach
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    botRuleForDeclarativeHost = new BotRuleForDeclarativeHost();
    Whitebox.setInternalState(botRuleForDeclarativeHost, botHostMatcher);
  }

  @Disabled
  @Test
  public void test_getBotFlag_hit222() throws Exception {
    String ip = "1.2.3.4";
    IpAttribute ipAttribute = new IpAttribute();
    ipAttribute.setIsAllAgentHoper(true);
    ipAttribute.setTotalCnt(301);
    ipAttribute.setClientIp(TransformUtil.ipToInt(ip));
    PowerMockito.when(botHostMatcher.isBotIp(ip)).thenReturn(true);

    int botFlag = botRuleForDeclarativeHost.getBotFlag(ipAttribute);
    Assertions.assertThat(botFlag).isEqualTo(0);
    Mockito.verify(botHostMatcher, Mockito.times(1)).isBotIp(ip);
  }

  @Test
  public void test_getBotFlag_not_hit222() throws Exception {
    String ip = "1.2.3.4";
    IpAttribute ipAttribute = new IpAttribute();
    ipAttribute.setIsAllAgentHoper(true);
    ipAttribute.setTotalCnt(300);
    ipAttribute.setClientIp(TransformUtil.ipToInt(ip));

    int botFlag = botRuleForDeclarativeHost.getBotFlag(ipAttribute);
    Assertions.assertThat(botFlag).isEqualTo(0);
  }
}
