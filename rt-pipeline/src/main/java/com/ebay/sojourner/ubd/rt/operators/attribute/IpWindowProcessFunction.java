package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Set;

public class IpWindowProcessFunction
        extends ProcessWindowFunction<IpAttributeAccumulator, Tuple2<String, Set<Integer>>, Tuple, TimeWindow> {

    @Override
    public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
                        Collector<Tuple2<String, Set<Integer>>> out) throws Exception {

        IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
        IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();

        if (context.currentWatermark() > context.window().maxTimestamp()) {
            out.collect(new Tuple2<>("ip" + ipAttribute.getClientIp(), null));
        } else if (context.currentWatermark() <= context.window().maxTimestamp() && ipAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
            out.collect(new Tuple2<>("ip" + ipAttribute.getClientIp(), ipAttribute.getBotFlagList()));
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
