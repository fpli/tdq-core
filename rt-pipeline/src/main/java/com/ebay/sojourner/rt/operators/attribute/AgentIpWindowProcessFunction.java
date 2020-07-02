package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

@Slf4j
public class AgentIpWindowProcessFunction extends
    ProcessWindowFunction<AgentIpAttributeAccumulator, AgentIpAttribute, Tuple, TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentIpAttributeAccumulator> elements,
      Collector<AgentIpAttribute> out) {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
    if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot6() < 0) {
      //      log.error(
      //          "agentIp windowfunction window: start" + context.window().getStart() + " end: "
      //          + context
      //              .window().getEnd());
      //      log.error("agentIp windowfunction scscount6<0:" + agentIpAttributeAccumulator
      //          .getAgentIpAttribute());
    }
    out.collect(agentIpAttributeAccumulator.getAgentIpAttribute());
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    System.out.println("agentIpWindowProcess thread id:" + Thread.currentThread().getId());
  }

  @Override
  public void clear(Context context) throws Exception {
    super.clear(context);
  }
}
