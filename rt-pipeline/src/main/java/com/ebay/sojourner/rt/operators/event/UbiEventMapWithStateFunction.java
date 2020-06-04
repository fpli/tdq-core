package com.ebay.sojourner.rt.operators.event;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Constants;
import org.apache.flink.streaming.runtime.operators.windowing.MapWithStateFunction;

public class UbiEventMapWithStateFunction
    implements MapWithStateFunction<UbiEvent, SessionAccumulator, UbiEvent> {

  @Override
  public UbiEvent map(UbiEvent value, SessionAccumulator sessionAccumulator) throws Exception {
    if (!value.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() == null) {
      sessionAccumulator.getUbiSession().setSessionId(value.getSessionId());
      sessionAccumulator.getUbiSession().setSessionSkey(value.getSessionSkey());
    } else if (value.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() != null) {
      long sessionSkey = value.getEventTimestamp() / Constants.SESSION_KEY_DIVISION;
      if (sessionSkey < sessionAccumulator.getUbiSession().getSessionSkey()) {
        sessionAccumulator.getUbiSession().setSessionSkey(sessionSkey);
      }
      value.setSessionId(sessionAccumulator.getUbiSession().getSessionId());
      value.setSessionSkey(sessionAccumulator.getUbiSession().getSessionSkey());
    } else if (value.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() == null) {
      value.updateSessionId();
      value.updateSessionSkey();
      sessionAccumulator.getUbiSession().setSessionId(value.getSessionId());
      sessionAccumulator.getUbiSession().setSessionSkey(value.getSessionSkey());
      sessionAccumulator.getUbiSession().setVersion(Constants.SESSION_VERSION);
    }
    return value;
  }
}