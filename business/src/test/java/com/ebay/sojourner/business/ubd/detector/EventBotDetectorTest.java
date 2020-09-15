package com.ebay.sojourner.business.ubd.detector;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.RuleCategory;
import com.ebay.sojourner.common.model.rule.RuleChangeEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.dsl.sql.SQLSessionRule;
import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

class EventBotDetectorTest {

  EventBotDetector eventBotDetector;

  @BeforeEach
  void setUp() {
    eventBotDetector = new EventBotDetector();
  }

  @Test
  void getBotFlagList() {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(123L);
    ruleDefinition.setContent("SELECT 123 FROM soj.idl_event WHERE agentInfo is not null");
    RuleChangeEvent event = new RuleChangeEvent();
    event.setLocalDateTime(LocalDateTime.now());
    event.setRules(Sets.newHashSet(ruleDefinition));
    eventBotDetector.onChange(event);

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setAgentInfo("abc");

    Set<Integer> botFlagList = eventBotDetector.getBotFlagList(ubiEvent);
    assertThat(botFlagList.size()).isEqualTo(1);
    assertThat(botFlagList.contains(123)).isTrue();
  }

  @Test
  void initBotRules() {
    eventBotDetector.initBotRules();
    List<SQLSessionRule> sqlRules = Whitebox.getInternalState(eventBotDetector, "sqlRules", EventBotDetector.class);
    assertThat(sqlRules.size()).isEqualTo(0);
  }

  @Test
  void onChange() {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(123L);
    ruleDefinition.setContent("SELECT 123 FROM soj.idl_event WHERE agentInfo is not null");
    RuleChangeEvent event = new RuleChangeEvent();
    event.setLocalDateTime(LocalDateTime.now());
    event.setRules(Sets.newHashSet(ruleDefinition));
    eventBotDetector.onChange(event);

    List<SQLSessionRule> sqlRules = Whitebox.getInternalState(eventBotDetector, "sqlRules", EventBotDetector.class);
    assertThat(sqlRules.size()).isEqualTo(1);
  }

  @Test
  void category() {
    RuleCategory category = eventBotDetector.category();
    assertThat(category).isEqualTo(RuleCategory.EVENT);
  }
}