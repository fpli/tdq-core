package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.Set;


public class UbiSessionWindowProcessFunction
        extends ProcessWindowFunction<SessionAccumulator, UbiEvent, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(UbiSessionWindowProcessFunction.class);
    private static SessionMetrics sessionMetrics;
    private OutputTag outputTag = null;

    public UbiSessionWindowProcessFunction(OutputTag outputTag) {
        this.outputTag = outputTag;

    }

    @Override
    public void process(Tuple tuple, Context context, Iterable<SessionAccumulator> elements,
                        Collector<UbiEvent> out) throws Exception {
        if (sessionMetrics == null) {
            sessionMetrics = new SessionMetrics();
        }

        SessionAccumulator sessionAccumulator = elements.iterator().next();

        if (sessionAccumulator.getUbiEvent() != null) {
            Set<Integer> eventBotFlagSet = sessionAccumulator.getUbiEvent().getBotFlags();
            UbiSession ubiSessionTmp = sessionAccumulator.getUbiSession();

            out.collect(sessionAccumulator.getUbiEvent());

            if (context.currentWatermark() > context.window().maxTimestamp()) {
                endSessionEvent(sessionAccumulator);
                UbiSession ubiSession = new UbiSession();
                ubiSession.setGuid(sessionAccumulator.getUbiEvent().getGuid());
                ubiSession.setAgentString(sessionAccumulator.getUbiSession().getAgentString());
                ubiSession.setSessionId(sessionAccumulator.getUbiSession().getSessionId());
                ubiSession.setIp(sessionAccumulator.getUbiSession().getIp());
                ubiSession.setUserAgent(sessionAccumulator.getUbiSession().getUserAgent());
                ubiSession.setExInternalIp(sessionAccumulator.getUbiSession().getExInternalIp());
                ubiSession.setSojDataDt(sessionAccumulator.getUbiSession().getSojDataDt());
                ubiSession.setSessionStartDt(sessionAccumulator.getUbiSession().getSessionStartDt());
                ubiSession.setAgentCnt(sessionAccumulator.getUbiSession().getAgentCnt());
                ubiSession.setStartTimestamp(sessionAccumulator.getUbiSession().getStartTimestamp());
                ubiSession.setEndTimestamp(sessionAccumulator.getUbiSession().getEndTimestamp());
                ubiSession.setAbsStartTimestamp(sessionAccumulator.getUbiSession().getAbsStartTimestamp());
                ubiSession.setAbsEndTimestamp(sessionAccumulator.getUbiSession().getAbsEndTimestamp());
                ubiSession.setClientIp(sessionAccumulator.getUbiSession().getClientIp());
                ubiSession.setInternalIp(sessionAccumulator.getUbiSession().getInternalIp());
                ubiSession.setSingleClickSessionFlag(sessionAccumulator.getUbiSession().getSingleClickSessionFlag());

                context.output(outputTag, ubiSession);
            }
        } else {
            logger.error("ubiEvent is null pls check");
        }
    }


    private void endSessionEvent(SessionAccumulator sessionAccumulator) throws Exception {
        if (sessionAccumulator.getUbiEvent().getEventTimestamp() != null) {
            sessionAccumulator.getUbiSession().setEndTimestamp(sessionAccumulator.getUbiEvent().getEventTimestamp());
        } else {
            logger.error(sessionAccumulator.getUbiEvent());
        }
        sessionMetrics.end(sessionAccumulator);

    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
//        InputStream configFile = getRuntimeContext().getDistributedCache().getClass().getResourceAsStream("configFile");
//        UBIConfig ubiConfig = UBIConfig.getInstance(configFile);

    }

}
