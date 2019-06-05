package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;

public class TimestampMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private PageIndicator indicator;

    // All timestamps are based on SOJ timestamp in microseconds


    @Override
    public void start(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (event.getIframe() == 0 && (event.getRdt() == 0 || 
                indicator.isCorrespondingPageEvent(event))) {
            if (sessionAccumulator.getUbiSession().getStartTimestamp() == null) {
                sessionAccumulator.getUbiSession().setStartTimestamp(event.getEventTimestamp());
            }
            if(sessionAccumulator.getUbiSession().getEndTimestamp()==null) {
                sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
            }
            else if(event.getEventTimestamp()!=null&&sessionAccumulator.getUbiSession().getEndTimestamp()<event.getEventTimestamp())
            {
                sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
            }
        }
        if(sessionAccumulator.getUbiSession().getAbsStartTimestamp()==null) {
            sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());
        }
        else if(event.getEventTimestamp()!=null&&sessionAccumulator.getUbiSession().getAbsStartTimestamp()>event.getEventTimestamp())
        {
            sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());
        }
        if(sessionAccumulator.getUbiSession().getAbsEndTimestamp()==null) {
            sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
        }
        else if(event.getEventTimestamp()!=null&&sessionAccumulator.getUbiSession().getAbsEndTimestamp()<event.getEventTimestamp())
        {
            sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
        }

    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {


        sessionAccumulator.getUbiSession().setSojDataDt(SOJTS2Date.castSojTimestampToDate(sessionAccumulator.getUbiSession().getAbsEndTimestamp()));
        // Fix bug HDMIT-3732 to avoid integer result overflow
        int durationSec = (sessionAccumulator.getUbiSession().getStartTimestamp() == null || sessionAccumulator.getUbiSession().getEndTimestamp() == null) ? 0
                : (int) ((sessionAccumulator.getUbiSession().getEndTimestamp() - sessionAccumulator.getUbiSession().getStartTimestamp()) / 1000000);
        int absDuration = (int) ((sessionAccumulator.getUbiSession().getAbsEndTimestamp() - sessionAccumulator.getUbiSession().getAbsStartTimestamp()) / 1000000);
        sessionAccumulator.getUbiSession().setDurationSec(durationSec);
        sessionAccumulator.getUbiSession().setAbsDuration(absDuration);
    }

    @Override
    public void init() throws Exception {
        setPageIndicator(new PageIndicator(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")).getString(Property.SEARCH_VIEW_PAGES)));
    }
    
    void setPageIndicator(PageIndicator indicator) {
        this.indicator = indicator;
    }
}
