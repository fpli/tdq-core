package com.ebay.tdq.sample;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.util.DateUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqMetricProcessWindowTagFunction
        extends ProcessWindowFunction<TdqMetric, TdqMetric, String, TimeWindow> {
    public final Map<Long, OutputTag<TdqMetric>> tagMap = new HashMap<>();

    public TdqMetricProcessWindowTagFunction() {
        for (String tag : new String[]{"10s", "30s", "60s"}) {
            Long seconds = DateUtils.toSeconds(tag);
            tagMap.put(seconds,
                    new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void process(String tag, Context context,
            Iterable<TdqMetric> iterable, Collector<TdqMetric> collector) {
        collect(iterable.iterator().next(), context);
    }

    void collect(TdqMetric m, Context context) {
        OutputTag<TdqMetric> outputTag = tagMap.get(m.getWindow());
        if (outputTag != null) {
            context.output(outputTag, m);
        } else {
            log.warn("Drop tdq metric{}, windows not supported!", m);
        }
    }
}
