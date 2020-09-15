package com.ebay.sojourner.business.ubd.detector;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiSession;
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

class NewSessionBotDetectorTest {

  NewSessionBotDetector newSessionBotDetector;

  @BeforeEach
  void setUp() {
    newSessionBotDetector = new NewSessionBotDetector();
  }

  @Test
  void getBotFlagList() throws Exception {

    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(123L);
    ruleDefinition.setContent("SELECT 1 FROM soj.idl_session WHERE viewCnt > 0");
    RuleChangeEvent event = new RuleChangeEvent();
    event.setLocalDateTime(LocalDateTime.now());
    event.setRules(Sets.newHashSet(ruleDefinition));
    newSessionBotDetector.onChange(event);

    UbiSession ubiSession = new UbiSession();
    ubiSession.setViewCnt(1);

    Set<Integer> botFlagList = newSessionBotDetector.getBotFlagList(ubiSession);

    assertThat(botFlagList.size()).isEqualTo(1);
    assertThat(botFlagList.contains(1)).isTrue();
  }

  @Test
  void initBotRules() {
    newSessionBotDetector.initBotRules();

    List<SQLSessionRule> sqlSessionRules = Whitebox.getInternalState(newSessionBotDetector, "sqlSessionRules", NewSessionBotDetector.class);

    assertThat(sqlSessionRules.size()).isEqualTo(0);
  }

  @Test
  void onChange() {
    RuleDefinition ruleDefinition = new RuleDefinition();
    ruleDefinition.setBizId(123L);
    ruleDefinition.setContent("SELECT 1 FROM soj.idl_session");
    RuleChangeEvent event = new RuleChangeEvent();
    event.setLocalDateTime(LocalDateTime.now());
    event.setRules(Sets.newHashSet(ruleDefinition));

    newSessionBotDetector.onChange(event);

    List<SQLSessionRule> sqlSessionRules = Whitebox.getInternalState(newSessionBotDetector, "sqlSessionRules", NewSessionBotDetector.class);
    assertThat(sqlSessionRules.size()).isEqualTo(1);
  }

  @Test
  void category() {
    RuleCategory category = newSessionBotDetector.category();
    assertThat(category).isEqualTo(RuleCategory.SESSION);
  }
}