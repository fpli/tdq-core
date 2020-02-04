package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.AgentIpSignatureBotDetector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Set;

public class AgentIpSignatureWindowProcessFunction
        extends ProcessWindowFunction<AgentIpAttributeAccumulator, AgentIpSignature, Tuple, TimeWindow> {
    private static final Logger logger = Logger.getLogger(AgentIpSignatureWindowProcessFunction.class);
    private AgentIpSignatureBotDetector agentIpSignatureBotDetector;
    private AgentIpSignature agentIpSignature;

    @Override
    public void process( Tuple tuple, Context context, Iterable<AgentIpAttributeAccumulator> elements,
                         Collector<AgentIpSignature> out ) throws Exception {

        AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
        AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();
        Set<Integer> botFlagList = agentIpSignatureBotDetector.getBotFlagList(agentIpAttribute);
        if (botFlagList != null && botFlagList.size() > 0) {

            agentIpSignature.getAgentIpBotSignature().put(agentIpAttribute.getAgent() + agentIpAttribute.getClientIp(), botFlagList);
            out.collect(agentIpSignature);
        }

    }

    @Override
    public void open( Configuration conf ) throws Exception {
        super.open(conf);
//        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        agentIpSignatureBotDetector = AgentIpSignatureBotDetector.getInstance();
        agentIpSignature = new AgentIpSignature();
    }

    @Override
    public void clear( Context context ) throws Exception {
        super.clear(context);
//        couchBaseManager.close();
    }
}
