package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UbiSessionHelper.class)
public class BotRuleForDeclarativeAgentTest {

    BotRuleForDeclarativeAgent botRuleForDeclarativeAgent;
    @Mock AgentAttribute agentAttribute;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        botRuleForDeclarativeAgent = new BotRuleForDeclarativeAgent();
        mockStatic(UbiSessionHelper.class);
    }

    @Test
    public void test_getBotFlag_hit() throws Exception {
        when(UbiSessionHelper.isAgentDeclarative(agentAttribute)).thenReturn(true);

        int botFlag = botRuleForDeclarativeAgent.getBotFlag(agentAttribute);
        Assertions.assertThat(botFlag).isEqualTo(221);
    }

    @Test
    public void test_getBotFlag_not_hit() throws Exception {
        when(UbiSessionHelper.isAgentDeclarative(agentAttribute)).thenReturn(false);

        int botFlag = botRuleForDeclarativeAgent.getBotFlag(agentAttribute);
        Assertions.assertThat(botFlag).isEqualTo(0);
    }
}