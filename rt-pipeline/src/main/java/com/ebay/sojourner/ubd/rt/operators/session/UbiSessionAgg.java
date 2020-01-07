package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.SessionBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class UbiSessionAgg implements AggregateFunction<UbiEvent, SessionAccumulator, SessionAccumulator> {
    private SessionMetrics sessionMetrics;
    private SessionBotDetector sessionBotDetector;
    //    private CouchBaseManager couchBaseManager;
//    private static final String BUCKET_NAME="botsignature";
    private static final Logger logger = Logger.getLogger(UbiSessionAgg.class);
    @Override
    public SessionAccumulator createAccumulator() {
        SessionAccumulator sessionAccumulator = new SessionAccumulator();
        sessionMetrics = SessionMetrics.getInstance();
        sessionBotDetector = SessionBotDetector.getInstance();
//        couchBaseManager = CouchBaseManager.getInstance();
        try {
            sessionMetrics.start(sessionAccumulator);
        } catch (Exception e) {
            log.error("session metrics start fail", e);
        }
        return sessionAccumulator;
    }

    @Override
    public SessionAccumulator add( UbiEvent value, SessionAccumulator accumulator ) {
        Set<Integer> eventBotFlagSet = value.getBotFlags();

        try {
            //move this logic to MapWithStateFunction
//            if (value.isNewSession() && accumulator.getUbiSession().getSessionId() == null) {
//                value.updateSessionId();
//            }
            sessionMetrics.feed(value, accumulator);
        } catch (Exception e) {
            log.error("start-session metrics collection issue:" + value, e);
        }
        if (accumulator.getUbiSession().getGuid() == null) {
            accumulator.getUbiSession().setGuid(value.getGuid());
        }
        Set<Integer> sessionBotFlagSetDetect = null;
        try {
            sessionBotFlagSetDetect = sessionBotDetector.getBotFlagList(accumulator.getUbiSession());
        } catch (IOException | InterruptedException e) {
            log.error("sessionBotDetector getBotFlagList error", e);
        }

        Set<Integer> sessionBotFlagSet = accumulator.getUbiSession().getBotFlagList();

//        Set<Integer> attrBotFlagWithIp = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getClientIp());
//        Set<Integer> attrBotFlagWithAgentIp = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getUserAgent()+accumulator.getUbiSession().getClientIp());
//        Set<Integer> attrBotFlagWithAgent = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getUserAgent());


        if (eventBotFlagSet != null && eventBotFlagSet.size() > 0 && !sessionBotFlagSet.containsAll(eventBotFlagSet)) {
            sessionBotFlagSet.addAll(eventBotFlagSet);
        }
        if (sessionBotFlagSetDetect != null && sessionBotFlagSetDetect.size() > 0) {

            sessionBotFlagSet.addAll(sessionBotFlagSetDetect);
            eventBotFlagSet.addAll(sessionBotFlagSetDetect);
        }
        if (value.getEventTimestamp() != null&&(accumulator.getUbiSession().getEndTimestamp()==null||value.getEventTimestamp()>accumulator.getUbiSession().getEndTimestamp())) {
            accumulator.getUbiSession().setEndTimestamp(value.getEventTimestamp());
        } else {
            logger.error(value);
        }
//        if(attrBotFlagWithAgentIp!=null&&attrBotFlagWithAgentIp.size()>0) {
//            sessionBotFlagSet.addAll(attrBotFlagWithAgentIp);
//            eventBotFlagSet.addAll(attrBotFlagWithAgentIp);
//        }
//        boolean isSuspectedAgent=false;
//        boolean isDeclarativeAgent=false;
//        boolean isDeclarativeHost=false;
//        boolean isSuspectedIp=false;
//        if(attrBotFlagWithIp!=null&&attrBotFlagWithIp.size()>0){
//            if(attrBotFlagWithIp.contains(222))
//            {
//                isDeclarativeHost=true;
//                attrBotFlagWithAgentIp.remove(222);
//            }
//            if(attrBotFlagWithIp.contains(223))
//            {
//                isSuspectedIp=true;
//                attrBotFlagWithAgentIp.remove(223);
//            }
//        }
//        if(attrBotFlagWithAgent!=null&&attrBotFlagWithAgent.size()>0){
//            if(attrBotFlagWithAgent.contains(220))
//            {
//                isSuspectedAgent=true;
//                attrBotFlagWithAgent.remove(220);
//            }
//            if(attrBotFlagWithAgent.contains(221))
//            {
//                isDeclarativeAgent=true;
//                attrBotFlagWithAgent.remove(221);
//            }
//        }
//        if((isSuspectedIp&&isDeclarativeAgent)||(isSuspectedAgent&&isDeclarativeHost))
//        {
//            attrBotFlagWithAgent.add(202);
//        }
//        if((isSuspectedAgent&&isDeclarativeAgent)||(isSuspectedIp&&isDeclarativeHost))
//        {
//            attrBotFlagWithIp.add(210);
//        }
//        if(isSuspectedIp)
//        {
//            attrBotFlagWithIp.add(211);
//        }
//        if(attrBotFlagWithIp!=null&&attrBotFlagWithIp.size()>0) {
//            sessionBotFlagSet.addAll(attrBotFlagWithIp);
//            eventBotFlagSet.addAll(attrBotFlagWithIp);
//        }
//        if(attrBotFlagWithAgent!=null&&attrBotFlagWithAgent.size()>0) {
//            sessionBotFlagSet.addAll(attrBotFlagWithAgent);
//            eventBotFlagSet.addAll(attrBotFlagWithAgent);
//        }
        accumulator.getUbiSession().setBotFlagList(sessionBotFlagSet);
        value.setBotFlags(eventBotFlagSet);
//        accumulator.setUbiEvent(value);

        return accumulator;
    }

    @Override
    public SessionAccumulator getResult( SessionAccumulator sessionAccumulator ) {
        return sessionAccumulator;
    }

    @Override
    public SessionAccumulator merge( SessionAccumulator a, SessionAccumulator b ) {
        log.info("SessionAccumulator merge:");
        a.setUbiSession(a.getUbiSession().merge(b.getUbiSession()));
        return a;
    }
}