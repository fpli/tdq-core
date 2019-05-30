package com.ebay.sojourner.ubd.common.sharedlib.metrics;


import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.log4j.Logger;

public class SessionMetrics extends RecordMetrics<UbiEvent, SessionAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static SessionMetrics sessionMetrics ;

    public static SessionMetrics getInstance() {
        if (sessionMetrics == null) {
            synchronized (SessionMetrics.class) {
                if (sessionMetrics == null) {
                    sessionMetrics = new SessionMetrics();
                }
            }
        }
        return sessionMetrics;
    }
    public SessionMetrics()  {

        initFieldMetrics();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void initFieldMetrics() {

        addFieldMetrics(new SingleClickFlagMetrics());
        addFieldMetrics(new AgentIPMetrics());
        addFieldMetrics(new AgentStringMetrics());
        addFieldMetrics(new SessionStartDtMetrics());
    }
}
