package com.ebay.sojourner.ubd.rt.operators.sessionizer;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseConnector;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.SessionBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

import java.util.Set;

public class UbiSessionAgg implements AggregateFunction<UbiEvent,SessionAccumulator,SessionAccumulator> {
    private  SessionMetrics sessionMetrics ;
    private  EventBotDetector eventBotDetector;
    private  SessionBotDetector sessionBotDetector;
    private static final Logger logger = Logger.getLogger(UbiSessionAgg.class);
    private CouchBaseConnector couchBaseConnector;
    private static final String BUCKET_NAME="botsignature";

    @Override
    public SessionAccumulator createAccumulator() {
        SessionAccumulator sessionAccumulator = new SessionAccumulator();
        sessionMetrics = SessionMetrics.getInstance();
        eventBotDetector=EventBotDetector.getInstance();
        sessionBotDetector=SessionBotDetector.getInstance();
        couchBaseConnector=CouchBaseConnector.getInstance();
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
        Set<Integer> eventBotFlagSet = value.getBotFlags();
        Set<Integer> eventBotFlagSetDetect=eventBotDetector.getBotFlagList(value);

        if(value.isNewSession()&&accumulator.getUbiSession().getSessionId()==null) {
            try {
                value.updateSessionId();
                sessionMetrics.feed(value,accumulator);

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

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("feed-session metrics collection issue:"+value);
                logger.error("feed-session metrics collection log:"+e.getMessage());
            }
        }
        Set<Integer> sessionBotFlagSetDetect=sessionBotDetector.getBotFlagList(accumulator.getUbiSession());
        Set<Integer> sessionBotFlagSet=accumulator.getUbiSession().getBotFlagList();
        Set<Integer> attrBotFlagSet = couchBaseConnector.scanSignature("ip",accumulator.getUbiSession().getClientIp(),"botFlag",BUCKET_NAME);

        if(sessionBotFlagSetDetect!=null&&sessionBotFlagSetDetect.size()>0) {
            sessionBotFlagSet.addAll(sessionBotFlagSetDetect);
            eventBotFlagSet.addAll(sessionBotFlagSetDetect);
        }
        if(attrBotFlagSet!=null&&attrBotFlagSet.size()>0) {
            sessionBotFlagSet.addAll(attrBotFlagSet);
            eventBotFlagSet.addAll(attrBotFlagSet);
        }

        if(eventBotFlagSetDetect!=null&&eventBotFlagSetDetect.size()>0) {
            eventBotFlagSet.addAll(eventBotFlagSetDetect);
        }

        accumulator.getUbiSession().setBotFlagList(sessionBotFlagSet);
        value.setBotFlags(eventBotFlagSet);
        accumulator.setUbiEvent(value);

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