package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector extends AbstractBotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;

  private EventBotDetector() {
  }

  public static EventBotDetector getInstance() {
    if (eventBotDetector == null) {
      synchronized (EventBotDetector.class) {
        if (eventBotDetector == null) {
          eventBotDetector = new EventBotDetector();
        }
      }
    }
    return eventBotDetector;
  }

  @Override
  public void initBotRules() {
    super.init();
  }
}
