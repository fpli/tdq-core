package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class AppIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private int appid;
    private boolean prior = false;
    private int firstAppid;

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setFirstAppId(Integer.MIN_VALUE);
        sessionAccumulator.getUbiSession().setAppId(Integer.MIN_VALUE);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (sessionAccumulator.getUbiSession().getFirstAppId() == Integer.MIN_VALUE && event.getAppId() != Integer.MIN_VALUE) {
            sessionAccumulator.getUbiSession().setFirstAppId(event.getAppId());
        }
        if (sessionAccumulator.getUbiSession().getAppId()==Integer.MIN_VALUE&&event.getIframe() == 0 && event.getRdt() == 0 && event.getAppId() != Integer.MIN_VALUE) {
            sessionAccumulator.getUbiSession().setAppId(event.getAppId());
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {
        if (sessionAccumulator.getUbiSession().getAppId() != Integer.MIN_VALUE) {
            sessionAccumulator.getUbiSession().setFirstAppId(sessionAccumulator.getUbiSession().getAppId());
        }
    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
