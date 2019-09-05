package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.SessionBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

public class UbiSessionAgg implements AggregateFunction<UbiEvent,SessionAccumulator,SessionAccumulator> {
    private  SessionMetrics sessionMetrics ;
    private  SessionBotDetector sessionBotDetector;
    private static final Logger logger = Logger.getLogger(UbiSessionAgg.class);
    private CouchBaseManager couchBaseManager;
    private static final String BUCKET_NAME="botsignature";

    @Override
    public SessionAccumulator createAccumulator() {
        SessionAccumulator sessionAccumulator = new SessionAccumulator();
        sessionMetrics = SessionMetrics.getInstance();
        sessionBotDetector=SessionBotDetector.getInstance();
        couchBaseManager = CouchBaseManager.getInstance();
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
        Set<Integer> sessionBotFlagSetDetect= null;
        try {
            sessionBotFlagSetDetect = sessionBotDetector.getBotFlagList(accumulator.getUbiSession());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<Integer> sessionBotFlagSet=accumulator.getUbiSession().getBotFlagList();
        Set<Integer> attrBotFlagWithIp = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getClientIp());
        Set<Integer> attrBotFlagWithAgentIp = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getUserAgent()+accumulator.getUbiSession().getClientIp());
        Set<Integer> attrBotFlagWithAgent = couchBaseManager.getSignatureWithDocId(accumulator.getUbiSession().getUserAgent());
        if(sessionBotFlagSetDetect!=null&&sessionBotFlagSetDetect.size()>0) {
            sessionBotFlagSet.addAll(sessionBotFlagSetDetect);
            eventBotFlagSet.addAll(sessionBotFlagSetDetect);
        }
        if(attrBotFlagWithAgentIp!=null&&attrBotFlagWithAgentIp.size()>0) {
            sessionBotFlagSet.addAll(attrBotFlagWithAgentIp);
            eventBotFlagSet.addAll(attrBotFlagWithAgentIp);
        }
        boolean isSuspectedAgent=false;
        boolean isDeclarativeAgent=false;
        boolean isDeclarativeHost=false;
        boolean isSuspectedIp=false;
        if(attrBotFlagWithIp!=null&&attrBotFlagWithIp.size()>0){
            if(attrBotFlagWithIp.contains(222))
            {
                isDeclarativeHost=true;
                attrBotFlagWithAgentIp.remove(222);
            }
            if(attrBotFlagWithIp.contains(223))
            {
                isSuspectedIp=true;
                attrBotFlagWithAgentIp.remove(223);
            }
        }
        if(attrBotFlagWithAgent!=null&&attrBotFlagWithAgent.size()>0){
            if(attrBotFlagWithAgent.contains(220))
            {
                isSuspectedAgent=true;
                attrBotFlagWithAgent.remove(220);
            }
            if(attrBotFlagWithAgent.contains(221))
            {
                isDeclarativeAgent=true;
                attrBotFlagWithAgent.remove(221);
            }
        }
        if((isSuspectedIp&&isDeclarativeAgent)||(isSuspectedAgent&&isDeclarativeHost))
        {
            attrBotFlagWithAgent.add(202);
        }
        if((isSuspectedAgent&&isDeclarativeAgent)||(isSuspectedIp&&isDeclarativeHost))
        {
            attrBotFlagWithIp.add(210);
        }
        if(isSuspectedIp)
        {
            attrBotFlagWithIp.add(211);
        }
        if(attrBotFlagWithIp!=null&&attrBotFlagWithIp.size()>0) {
            sessionBotFlagSet.addAll(attrBotFlagWithIp);
            eventBotFlagSet.addAll(attrBotFlagWithIp);
        }
        if(attrBotFlagWithAgent!=null&&attrBotFlagWithAgent.size()>0) {
            sessionBotFlagSet.addAll(attrBotFlagWithAgent);
            eventBotFlagSet.addAll(attrBotFlagWithAgent);
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
         a.setUbiSession(a.getUbiSession().merge(b.getUbiSession()));
       return a;
    }
}