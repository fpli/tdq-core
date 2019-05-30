package com.ebay.sojourner.ubd.common.sharedlib.metrics;


import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJListGetValueByIndex;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.regex.Pattern;

public class SessionStartDtMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    private static final Logger logger = Logger.getLogger(SingleClickFlagMetrics.class);
    private long sessionStartDt;
    private Integer seqNum;
    @Override
    public void init() throws Exception {
        // nothing to do
        sessionStartDt=0;
        seqNum=0;
    }
    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionStartDt=0;
        seqNum=0;
//        feed(event, sessionAccumulator);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
         seqNum++;
        if (event.getIframe() == 0) {

            if (event.getRdt() == 0) {
               if(sessionStartDt==0)
               {
                   sessionStartDt=event.getSojDataDt();

                   sessionAccumulator.getUbiSession().setSessionStartDt(sessionStartDt);
               }

            }
        }
        if(!event.isNewSession()&&sessionAccumulator.getUbiSession().getSessionId()==null)
        {
            sessionAccumulator.getUbiSession().setSessionId(event.getSessionId());
        }
        else if(event.isNewSession()&&sessionAccumulator.getUbiSession().getSessionId()!=null)
        {
            event.setSessionId(sessionAccumulator.getUbiSession().getSessionId());
        }
        event.setSessionStartDt(sessionStartDt);
        event.setSeqNum(seqNum);
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

}
