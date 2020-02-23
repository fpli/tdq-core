package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Set;

public class AgentWindowProcessFunction
        extends ProcessWindowFunction<AgentAttributeAccumulator, Tuple2<String, Set<Integer>>, Tuple, TimeWindow> {

    @Override
    public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
                        Collector<Tuple2<String, Set<Integer>>> out) throws Exception {

        AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
        AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();

        if (context.currentWatermark() > context.window().maxTimestamp()) {
            out.collect(new Tuple2<>("agent" + agentAttribute.getAgent(), null));
        } else if (context.currentWatermark() <= context.window().maxTimestamp() && agentAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
            out.collect(new Tuple2<>("agent" + agentAttribute.getAgent(), agentAttribute.getBotFlagList()));
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
