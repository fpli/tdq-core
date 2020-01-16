package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.FlagUtils;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class BidCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private static PageIndicator indicator;

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setBidCoreCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (event.getIframe() == 0 && event.getRdt() == 0 &&
                indicator.isCorrespondingPageEvent(event) &&
                FlagUtils.matchFlag(event, 3, 1)) {
            sessionAccumulator.getUbiSession().setBidCoreCnt(sessionAccumulator.getUbiSession().getBidCoreCnt()+1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void init() throws Exception {
        setPageIndicator(new PageIndicator(UBIConfig.getString(Property.BID_PAGES)));
    }

    void setPageIndicator(PageIndicator indicator) {
        this.indicator = indicator;
    }
}
