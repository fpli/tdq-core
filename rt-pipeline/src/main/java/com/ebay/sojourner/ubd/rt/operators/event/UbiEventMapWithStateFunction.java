package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.flink.streaming.runtime.operators.windowing.MapWithStateFunction;

public class UbiEventMapWithStateFunction implements MapWithStateFunction<UbiEvent, SessionAccumulator, UbiEvent> {
    @Override
    public UbiEvent map( UbiEvent value, SessionAccumulator sessionAccumulator ) throws Exception {
        if (value.isNewSession() && sessionAccumulator.getUbiSession().getSessionId() == null) {
            value.updateSessionId();
            sessionAccumulator.getUbiSession().setSessionId(value.getSessionId());
        }

        return value;

    }
}
