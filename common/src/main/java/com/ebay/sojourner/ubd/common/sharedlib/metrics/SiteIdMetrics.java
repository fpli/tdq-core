package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class SiteIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private boolean isFirst = true;
    private Integer siteId;

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        this.isFirst = true;
        this.siteId = null;
        sessionAccumulator.getUbiSession().setFirstSiteId(null);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        if (sessionAccumulator.getUbiSession().getFirstSiteId() == null && event.getIframe() == 0 && event.getRdt() == 0) {
            sessionAccumulator.getUbiSession().setFirstSiteId(event.getSiteId());
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {

    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
