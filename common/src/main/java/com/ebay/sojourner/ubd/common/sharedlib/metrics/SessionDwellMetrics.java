package com.ebay.sojourner.ubd.common.sharedlib.metrics;


import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.log4j.Logger;

public class SessionDwellMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    private static final Logger logger = Logger.getLogger(SingleClickFlagMetrics.class);
    private Long[] minMaxEventTimestamp ;
    private Integer seqNum;
    @Override
    public void init() throws Exception {

    }
    @Override
    public void start(SessionAccumulator sessionAccumulator) {

        minMaxEventTimestamp = new Long[]{Long.MAX_VALUE,Long.MIN_VALUE};
        sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
//        feed(event, sessionAccumulator);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        long eventTimestamp = event.getEventTimestamp();
        if (event.getIframe() != null && event.getRdt() != null && event.getIframe() == 0 && event.getRdt() == 0) {
            minMaxEventTimestamp = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
            if (minMaxEventTimestamp[0] > eventTimestamp) {
                minMaxEventTimestamp[0] = eventTimestamp;
            }
            if (minMaxEventTimestamp[1] < eventTimestamp) {
                minMaxEventTimestamp[1] = eventTimestamp;
            }
            sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

}
