package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.operators.mertrics.SessionMetrics;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class UbiSessionAggregation implements AggregateFunction<UbiEvent,UbiEvent,UbiEvent> {
    private static SessionMetrics sessionMetrics = new SessionMetrics();
    private static final Logger logger = Logger.getLogger(UbiSessionAggregation.class);

    @Override
    public UbiEvent createAccumulator() {

        return new UbiEvent();
    }

    @Override
    public UbiEvent add(UbiEvent value, UbiEvent accumulator) {
        accumulator = value;
        if(accumulator.isNewSession()) {
            try {
                sessionMetrics.start(accumulator,accumulator);
            } catch (Exception e) {
                logger.info("start-session metrics collection issue:"+accumulator.getGuid());
            }
        }
        else
        {
            try {
                sessionMetrics.feed(accumulator,accumulator);
            } catch (Exception e) {
                logger.info("feed-session metrics collection issue:"+accumulator.getGuid());
            }
        }
      return accumulator;
    }

    @Override
    public UbiEvent getResult(UbiEvent accumulator) {
       return accumulator;
    }

    @Override
    public UbiEvent merge(UbiEvent a, UbiEvent b) {

    return null;
    }
}