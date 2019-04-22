package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.SessionAccumulator;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.operators.metrics.SessionMetrics;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class UbiSessionAgg implements AggregateFunction<UbiEvent,SessionAccumulator,SessionAccumulator> {
    private static SessionMetrics sessionMetrics ;
    private static final Logger logger = Logger.getLogger(UbiSessionAgg.class);

    @Override
    public SessionAccumulator createAccumulator() {
        SessionAccumulator sessionAccumulator = new SessionAccumulator();
        sessionMetrics = SessionMetrics.getInstance();
        try {
            sessionMetrics.start(sessionAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return sessionAccumulator;
    }

    @Override
    public SessionAccumulator add(UbiEvent value, SessionAccumulator accumulator) {
        if(value.isNewSession()) {
            try {
                value.updateSessionId();
                sessionMetrics.feed(value,accumulator);
                accumulator.setUbiEvent(value);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("start-session metrics collection issue:"+value);
                logger.error("start-session metrics collection log:"+e.getMessage());
            }
        }
        else
        {
            try {
                sessionMetrics.feed(value,accumulator);
                accumulator.setUbiEvent(value);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("feed-session metrics collection issue:"+value);
                logger.error("feed-session metrics collection log:"+e.getMessage());
            }
        }
      return accumulator;
    }

    @Override
    public SessionAccumulator getResult(SessionAccumulator sessionAccumulator) {
       return sessionAccumulator;
    }

    @Override
    public SessionAccumulator merge(SessionAccumulator a, SessionAccumulator b) {
        logger.error("SessionAccumulator merge:");
    return null;
    }
}