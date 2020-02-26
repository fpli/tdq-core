package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Set;

public class IpWindowProcessFunction
        extends ProcessWindowFunction<IpAttributeAccumulator, Tuple3<String, Set<Integer>, Long>, Tuple, TimeWindow> {

    @Override
    public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
                        Collector<Tuple3<String, Set<Integer>, Long>> out) throws Exception {

        IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
        IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();

        if (context.currentWatermark() > context.window().maxTimestamp()
                && ipAttribute.getBotFlagList() != null && ipAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple3<>("ip" + ipAttribute.getClientIp(), null, context.window().maxTimestamp()));
        } else if (context.currentWatermark() <= context.window().maxTimestamp() && ipAttributeAccumulator.getBotFlagStatus().containsValue(1)
                && ipAttribute.getBotFlagList() != null && ipAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple3<>("ip" + ipAttribute.getClientIp(), ipAttribute.getBotFlagList(), context.window().maxTimestamp()));
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
