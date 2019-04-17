package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.operators.mertrics.SessionMetrics;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UbiSessionReducer implements ReduceFunction<UbiEvent> {

    private static SessionMetrics sessionMetrics = new SessionMetrics();
    @Override
    public UbiEvent reduce(UbiEvent currentValue, UbiEvent newValue) throws Exception {
        if (currentValue.isNewSession() && newValue.isNewSession()) {
            currentValue.updateSessionId();
            sessionMetrics.start(currentValue,currentValue);
            // merge this event time as they are in the session.
        }

        newValue.setSessionStartTime(currentValue.getSessionStartTime());
        newValue.setSessionEndTime(newValue.getEventTimestamp());
        newValue.setSessionId(currentValue.getSessionId());
        newValue.setEventCnt(currentValue.getEventCnt());
        newValue.setUbiSession(currentValue.getUbiSession());
        newValue.eventCountIncrementByOne();
        sessionMetrics.feed(newValue,newValue);

        return newValue;
    }
}
