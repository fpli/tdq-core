package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.AgentIpSignatureBotDetector;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.Set;

public class AgentIpMapFunction extends RichMapFunction<AgentIpAttribute, AgentIpSignature> {
    private static final Logger logger = Logger.getLogger(AgentIpMapFunction.class);
    private AgentIpSignatureBotDetector agentIpSignatureBotDetector;
    private AgentIpSignature agentIpSignature;

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
//        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        agentIpSignatureBotDetector = AgentIpSignatureBotDetector.getInstance();
        agentIpSignature = new AgentIpSignature();
    }


    @Override
    public AgentIpSignature map(AgentIpAttribute value) throws Exception {
        Set<Integer> botFlagList = agentIpSignatureBotDetector.getBotFlagList(value);
        agentIpSignature.getAgentIpBotSignature().put(value.getAgent() + value.getClientIp(), botFlagList);
        return agentIpSignature;
    }
}
