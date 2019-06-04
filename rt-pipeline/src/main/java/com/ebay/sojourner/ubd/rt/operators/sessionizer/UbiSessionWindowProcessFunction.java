package com.ebay.sojourner.ubd.rt.operators.sessionizer;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.detector.SignatureBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.queryablestate.client.QueryableStateClient;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Set;


public class UbiSessionWindowProcessFunction
        extends ProcessWindowFunction<SessionAccumulator, UbiEvent, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(UbiSessionWindowProcessFunction.class);
    private static SessionMetrics sessionMetrics;
    private OutputTag outputTag = null;
    private JobID jobID = null;
    private String proxyHost = "127.0.0.1";
    private int proxyPort = 9069;
    private QueryableStateClient client = null;
    private static SignatureBotDetector singnatureBotDetector;

    public UbiSessionWindowProcessFunction(OutputTag outputTag, JobID jobID) throws UnknownHostException {
        this.outputTag = outputTag;
        this.jobID = jobID;
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

            Set<Integer> botFlagSet = singnatureBotDetector.getBotFlagList(ubiSessionTmp);
            eventBotFlagSet.addAll(botFlagSet);
            sessionAccumulator.getUbiEvent().setBotFlags(eventBotFlagSet);
            out.collect(sessionAccumulator.getUbiEvent());
            Set<Integer> sessionBotFlagSet = sessionAccumulator.getUbiSession().getBotFlagList();
            sessionBotFlagSet.addAll(botFlagSet);
            sessionAccumulator.getUbiSession().setBotFlagList(sessionBotFlagSet);
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
                ubiSession.setAgentCnt(sessionAccumulator.getUbiSession().getAgentCnt());
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
        File configFile = getRuntimeContext().getDistributedCache().getFile("configFile");
        UBIConfig ubiConfig = UBIConfig.getInstance(configFile);

        this.client = new QueryableStateClient(proxyHost, proxyPort);
        this.client.setExecutionConfig(getRuntimeContext().getExecutionConfig());

        singnatureBotDetector = SignatureBotDetector.getInstance(this.client,this.jobID);

    }

//    private CompletableFuture<ValueState<IpSignature>> queryState(String key, JobID jobId, QueryableStateClient client) {
//        return client.getKvState(
//                jobId,
//                "bot7",
//                key,
//                Types.STRING,
//                new ValueStateDescriptor<IpSignature>("bot7", TypeInformation.of(new TypeHint<IpSignature>() {
//                })));
//    }


}
