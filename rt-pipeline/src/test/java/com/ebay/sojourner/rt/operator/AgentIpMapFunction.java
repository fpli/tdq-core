package com.ebay.sojourner.rt.operator;

import com.ebay.sojourner.business.detector.AgentIpSignatureBotDetector;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpSignature;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class AgentIpMapFunction extends RichMapFunction<AgentIpAttribute, AgentIpSignature> {

  private AgentIpSignatureBotDetector agentIpSignatureBotDetector;
  private AgentIpSignature agentIpSignature;

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    agentIpSignatureBotDetector = AgentIpSignatureBotDetector.getInstance();
    agentIpSignature = new AgentIpSignature();
  }

  @Override
  public AgentIpSignature map(AgentIpAttribute value) throws Exception {
    Set<Integer> botFlagList = agentIpSignatureBotDetector.getBotFlagList(value);
    agentIpSignature
        .getAgentIpBotSignature()
        .put(value, botFlagList);
    return agentIpSignature;
  }
}
