package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionStartDtMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private long sessionStartDt;
  private Integer seqNum;

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setSessionStartDt(0L);
    sessionAccumulator.getUbiSession().setSeqNum(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    sessionAccumulator
        .getUbiSession()
        .setSeqNum(sessionAccumulator.getUbiSession().getSeqNum() + 1);
    sessionAccumulator.getUbiSession().setFirstSessionStartDt(event.getSojDataDt());
    if (!event.isIframe()
        && !event.isRdt()
        && sessionAccumulator.getUbiSession().getSessionStartDt() == 0) {
      sessionAccumulator.getUbiSession().setSessionStartDt(event.getSojDataDt());
    }

    if (!event.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() == null) {
      sessionAccumulator.getUbiSession().setSessionId(event.getSessionId());
      sessionAccumulator.getUbiSession().setSessionSkey(event.getSessionSkey());
    } else if (event.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() != null) {
      event.setSessionId(sessionAccumulator.getUbiSession().getSessionId());
      event.setSessionSkey(sessionAccumulator.getUbiSession().getSessionSkey());
    } else if (event.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() == null) {
      event.updateSessionId();
      event.updateSessionSkey();
      sessionAccumulator.getUbiSession().setSessionId(event.getSessionId());
      sessionAccumulator.getUbiSession().setSessionSkey(event.getSessionSkey());
      sessionAccumulator.getUbiSession().setVersion(Constants.SESSION_VERSION);
    }

    event.setSessionStartDt(
        sessionAccumulator.getUbiSession().getSessionStartDt() == 0L ? sessionAccumulator
            .getUbiSession().getFirstSessionStartDt()
            : sessionAccumulator.getUbiSession().getSessionStartDt());
    event.setSeqNum(sessionAccumulator.getUbiSession().getSeqNum());
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }
}
