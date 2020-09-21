package com.ebay.sojourner.business.rule;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotRuleForNewBotTest {

  BotRuleForNewBot botRuleForNewBot;
  AgentIpAttribute agentIpAttribute;

  @BeforeEach
  void setUp() {
    botRuleForNewBot = new BotRuleForNewBot();
    agentIpAttribute = new AgentIpAttribute();
  }

  @Test
  void getBotFlag_hit() {
    agentIpAttribute.setHasNewBot(true);
    int botFlag = botRuleForNewBot.getBotFlag(agentIpAttribute);
    assertThat(botFlag).isEqualTo(224);
  }

  @Test
  void getBotFlag_not_hit() {
    agentIpAttribute.setHasNewBot(false);
    int botFlag = botRuleForNewBot.getBotFlag(agentIpAttribute);
    assertThat(botFlag).isEqualTo(0);
  }
}