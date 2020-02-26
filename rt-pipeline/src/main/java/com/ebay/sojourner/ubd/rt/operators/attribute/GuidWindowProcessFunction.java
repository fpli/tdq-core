package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.GuidAttribute;
import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Set;

public class GuidWindowProcessFunction
        extends ProcessWindowFunction<GuidAttributeAccumulator, Tuple3<String, Set<Integer>, Long>, Tuple, TimeWindow> {

    @Override
    public void process(Tuple tuple, Context context, Iterable<GuidAttributeAccumulator> elements,
                        Collector<Tuple3<String, Set<Integer>, Long>> out) throws Exception {

        GuidAttributeAccumulator guidAttributeAccumulator = elements.iterator().next();
        GuidAttribute guidAttribute = guidAttributeAccumulator.getGuidAttribute();

        if (context.currentWatermark() > context.window().maxTimestamp()
                && guidAttribute.getBotFlagList() != null && guidAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple3<>("guid" + guidAttribute.getGuid(), null, context.window().maxTimestamp()));
        } else if (context.currentWatermark() <= context.window().maxTimestamp() && guidAttributeAccumulator.getFlag() == 1
                && guidAttribute.getBotFlagList() != null && guidAttribute.getBotFlagList().size() > 0) {
            out.collect(new Tuple3<>("guid" + guidAttribute.getGuid(), guidAttribute.getBotFlagList(), context.window().maxTimestamp()));
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
