package com.ebay.sojourner.business.ubd.detector;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BotDetectorsTest {

  @Test
  void getSessionBotDetector() {
    NewSessionBotDetector sessionBotDetector = BotDetectors.getSessionBotDetector();
    NewSessionBotDetector sessionBotDetector1 = BotDetectors.getSessionBotDetector();
    Assertions.assertThat(sessionBotDetector == sessionBotDetector1)
        .isTrue();
  }
}