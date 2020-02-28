package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgentWindowProcessFunction
        extends ProcessWindowFunction<AgentAttributeAccumulator, Tuple4<String, Boolean, Set<Integer>, Long>, Tuple, TimeWindow> {

    private HashSet<Integer> generationBotFlag;

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
                        Collector<Tuple4<String, Boolean, Set<Integer>, Long>> out) throws Exception {

        AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
        AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();

        if (context.currentWatermark() >= context.window().maxTimestamp()
                && agentAttribute.getBotFlagList() != null && agentAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple4<>("agent" + agentAttribute.getAgent(), false, agentAttribute.getBotFlagList(),
                    context.window().maxTimestamp()));
        } else if (context.currentWatermark() < context.window().maxTimestamp()
                && agentAttributeAccumulator.getBotFlagStatus().containsValue(1)
                && agentAttribute.getBotFlagList() != null && agentAttribute.getBotFlagList().size() > 0) {
            generationBotFlag = new HashSet<>();
            for (Map.Entry<Integer, Integer> newBotFlagMap : agentAttributeAccumulator.getBotFlagStatus().entrySet()) {
                if (newBotFlagMap.getValue() == 1) {
                    generationBotFlag.add(newBotFlagMap.getKey());
                }
            }
            out.collect(new Tuple4<>("agent" + agentAttribute.getAgent(), true, generationBotFlag,
                    context.window().maxTimestamp()));
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
