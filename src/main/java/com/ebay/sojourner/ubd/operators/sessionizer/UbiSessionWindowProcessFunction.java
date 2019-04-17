package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.model.UbiSession;
import com.ebay.sojourner.ubd.operators.mertrics.SessionMetrics;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class UbiSessionWindowProcessFunction
        extends ProcessWindowFunction<UbiEvent, UbiEvent, Tuple, TimeWindow> {

    private static SessionMetrics sessionMetrics =  new SessionMetrics();
    private OutputTag outputTag =null;
    public UbiSessionWindowProcessFunction(OutputTag outputTag)
    {
        this.outputTag=outputTag;
    }
    @Override
    public void process(Tuple tuple, Context context, Iterable<UbiEvent> elements,
                        Collector<UbiEvent> out) throws Exception {
        UbiEvent sessionEvent = elements.iterator().next();
        updateSessionEvent(sessionEvent);
        out.collect(sessionEvent);


        if (context.currentWatermark() > context.window().maxTimestamp()) {
            endSessionEvent(sessionEvent);
            UbiSession ubiSession = new UbiSession();
            ubiSession.setGuid(sessionEvent.getGuid());
            ubiSession.setAgentString(sessionEvent.getUbiSession().getAgentString());
            ubiSession.setIp(sessionEvent.getUbiSession().getIp());
            ubiSession.setUserAgent(sessionEvent.getUbiSession().getUserAgent());
            ubiSession.setExInternalIp(sessionEvent.getUbiSession().getExInternalIp());
            ubiSession.setAgentCnt(sessionEvent.getUbiSession().getAgentCnt());
            ubiSession.setSingleClickSessionFlag(sessionEvent.getUbiSession().getSingleClickSessionFlag());
           context.output(outputTag,ubiSession);
        }
    }
    private void endSessionEvent(UbiEvent sessionEvent) throws Exception {
        sessionEvent.getUbiSession().setEndTimestamp(sessionEvent.getEventTimestamp());
        sessionMetrics.end(sessionEvent);

    }

    private void updateSessionEvent(UbiEvent sessionEvent) throws Exception {
        // This is true only when the session has one event
        if (sessionEvent.isNewSession()) {
            sessionEvent.updateSessionId();
            sessionMetrics.start(sessionEvent,sessionEvent);
        }
    }
}
