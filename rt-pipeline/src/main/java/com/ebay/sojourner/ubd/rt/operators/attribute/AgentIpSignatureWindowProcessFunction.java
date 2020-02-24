package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Set;

public class AgentIpSignatureWindowProcessFunction
        extends ProcessWindowFunction<AgentIpAttributeAccumulator, Tuple3<String, Set<Integer>, Long>, Tuple, TimeWindow> {

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentIpAttributeAccumulator> elements,
                        Collector<Tuple3<String, Set<Integer>, Long>> out) throws Exception {

        AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
        AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();

        if (context.currentWatermark() > context.window().maxTimestamp()) {
            out.collect(new Tuple3<>("agentIp" + agentIpAttribute.getAgent() + agentIpAttribute.getClientIp(), null, context.window().maxTimestamp()));
        } else if (context.currentWatermark() <= context.window().maxTimestamp() && agentIpAttributeAccumulator.getBotFlagStatus().containsValue(1)
                && agentIpAttribute.getBotFlagList() != null && agentIpAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple3<>("agentIp" + agentIpAttribute.getAgent() + agentIpAttribute.getClientIp(), agentIpAttribute.getBotFlagList(), context.window().maxTimestamp()));
        }
    }

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
    }

    @Override
    public void clear(Context context) throws Exception {
        super.clear(context);
    }
}
