package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SOJNVL;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class FirstMappedUserIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFirstMappedUserId(null);
  }

  /**
   * get first valid bu tag in application_payload of valid events
   */
  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyValidEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    String bestGuessUserId = null;
    if ((isEarlyValidEvent ? isEarlyValidEvent
        : sessionAccumulator.getUbiSession().getFirstMappedUserId() == null)
        && !event.isRdt()
        && !event.isIframe()) {
      bestGuessUserId = SOJNVL.getTagValue(event.getApplicationPayload(), "bu");
      try {
        bestGuessUserId = bestGuessUserId.trim();
        if (bestGuessUserId.length() <= 18) {
          Long validUserId = Long.parseLong(bestGuessUserId);
          if (validUserId > 0 && validUserId != 3564) {
            sessionAccumulator.getUbiSession().setFirstMappedUserId(validUserId);
          }
        }
      } catch (NumberFormatException e1) {
        // ignore
      } catch (NullPointerException e2) {
        // ignore
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
