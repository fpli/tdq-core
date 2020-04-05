package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionForGuidEnhancement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuidIndicators extends
    AttributeIndicators<SessionForGuidEnhancement, GuidAttributeAccumulator> {

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
