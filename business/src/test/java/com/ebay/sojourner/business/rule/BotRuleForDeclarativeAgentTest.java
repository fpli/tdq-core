package com.ebay.sojourner.business.rule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.util.UbiSessionHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UbiSessionHelper.class)
public class BotRuleForDeclarativeAgentTest {

  BotRuleForDeclarativeAgent botRuleForDeclarativeAgent;
  AgentAttribute agentAttribute = mock(AgentAttribute.class);

  @Before
  public void setup() throws Exception {
    botRuleForDeclarativeAgent = new BotRuleForDeclarativeAgent();
    mockStatic(UbiSessionHelper.class);
  }

  @Test
  public void test_getBotFlag_hit() throws Exception {
    when(UbiSessionHelper.isAgentDeclarative(agentAttribute)).thenReturn(true);
    int botFlag = botRuleForDeclarativeAgent.getBotFlag(agentAttribute);
    assertThat(botFlag).isEqualTo(221);
  }

  @Test
  public void test_getBotFlag_not_hit() throws Exception {
    when(UbiSessionHelper.isAgentDeclarative(agentAttribute)).thenReturn(false);
    int botFlag = botRuleForDeclarativeAgent.getBotFlag(agentAttribute);
    assertThat(botFlag).isEqualTo(0);
  }
}
