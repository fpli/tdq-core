package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class UbiSessionWindowProcessFunction
        extends ProcessWindowFunction<UbiEvent, UbiEvent, Tuple, TimeWindow> {
    @Override
    public void process(Tuple tuple, Context context, Iterable<UbiEvent> elements,
                        Collector<UbiEvent> out) throws Exception {

    }
}
