package com.ebay.sojourner.business.detector;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.rule.Rule;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

class GuidSignatureBotDetectorTest {

  GuidSignatureBotDetector guidSignatureBotDetector;

  @BeforeEach
  void setUp() {
    guidSignatureBotDetector = GuidSignatureBotDetector.getInstance();
  }

  @Test
  void getBotFlagList() throws Exception {
    GuidAttribute guidAttribute = new GuidAttribute();
    guidAttribute.setAbsEventCount(10001);

    Set<Integer> result = guidSignatureBotDetector.getBotFlagList(guidAttribute);
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.contains(15)).isTrue();
  }

  @Test
  void initBotRules() {
    Set<Rule> botRules = Whitebox.getInternalState(guidSignatureBotDetector, "botRules", GuidSignatureBotDetector.class);
    assertThat(botRules.size()).isEqualTo(1);
  }
}