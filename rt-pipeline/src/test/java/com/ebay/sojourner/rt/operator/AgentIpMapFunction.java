package com.ebay.sojourner.rt.operator;

import com.ebay.sojourner.business.ubd.detectors.AgentIpSignatureBotDetector;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpSignature;
import java.util.Set;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

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
    //        System.out.println(value);
    Set<Integer> botFlagList = agentIpSignatureBotDetector.getBotFlagList(value);
    agentIpSignature
        .getAgentIpBotSignature()
        .put(value, botFlagList);
    return agentIpSignature;
  }
}