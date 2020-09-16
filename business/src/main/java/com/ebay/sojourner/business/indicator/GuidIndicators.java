package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuidIndicators extends
    AttributeIndicators<SessionCore, GuidAttributeAccumulator> {

  private static volatile GuidIndicators guidIndicators;

  public GuidIndicators() {
    initIndicators();
    try {
      init();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public static GuidIndicators getInstance() {
    if (guidIndicators == null) {
      synchronized (GuidIndicators.class) {
        if (guidIndicators == null) {
          guidIndicators = new GuidIndicators();
        }
      }
    }
    return guidIndicators;
  }

  @Override
  public void initIndicators() {

    addIndicators(new AbsEventCountIndicator());
  }
}
