package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class AppIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private Integer appid;
    private boolean prior = false;
    private Integer firstAppid;

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setFirstAppId(null);
        sessionAccumulator.getUbiSession().setAppId(null);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (sessionAccumulator.getUbiSession().getFirstAppId() == null && event.getAppId() != null) {
            sessionAccumulator.getUbiSession().setFirstAppId(event.getAppId());
        }
        if (sessionAccumulator.getUbiSession().getAppId() == null && event.getIframe() == 0 && event.getRdt() == 0 && event.getAppId() != null) {
            sessionAccumulator.getUbiSession().setAppId(event.getAppId());
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {
        if (sessionAccumulator.getUbiSession().getAppId() != null) {
            sessionAccumulator.getUbiSession().setFirstAppId(sessionAccumulator.getUbiSession().getAppId());
        }
    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
