package com.ebay.sojourner.ubd.rt.operators.sessionizer;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detector.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.detector.SessionBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class UbiSessionAgg implements AggregateFunction<UbiEvent,SessionAccumulator,SessionAccumulator> {
    private static SessionMetrics sessionMetrics ;
    private static EventBotDetector eventBotDetector;
    private static SessionBotDetector sessionBotDetector;
    private static final Logger logger = Logger.getLogger(UbiSessionAgg.class);

    @Override
    public SessionAccumulator createAccumulator() {
        SessionAccumulator sessionAccumulator = new SessionAccumulator();
        sessionMetrics = SessionMetrics.getInstance();
        eventBotDetector=EventBotDetector.getInstance();
        sessionBotDetector=SessionBotDetector.getInstance();
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
        Set<Integer> botFlagList=eventBotDetector.getBotFlagList(value);
        value.setBotFlags(botFlagList);
        if(value.isNewSession()&&accumulator.getUbiSession().getSessionId()==null) {
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
        Set<Integer> sessionBotFlagList=sessionBotDetector.getBotFlagList(accumulator.getUbiSession());
        accumulator.getUbiSession().setBotFlagList(sessionBotFlagList);
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