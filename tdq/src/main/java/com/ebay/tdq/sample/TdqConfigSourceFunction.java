package com.ebay.tdq.sample;

import com.ebay.tdq.config.TdqConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * TODO get from DB or http request
 *
 * @author juntzhang
 */
public class TdqConfigSourceFunction extends RichSourceFunction<TdqConfig> {
    private final String baseURL;
    private final Long   interval;
    private final String env;

    public TdqConfigSourceFunction(String baseURL, Long interval, String env) {
        this.baseURL  = baseURL;
        this.interval = interval;
        this.env      = env;
    }

    @Override
    public void run(SourceContext<TdqConfig> ctx) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TdqConfig config = objectMapper
                .reader().forType(TdqConfig.class)
                .readValue(new File(
                        "/Users/juntzhang/src/TDQ/sojourner-core" +
                                "/tdq/src/test/resources/q2/example3.json"));
        while (true) {
            ctx.collectWithTimestamp(config, System.currentTimeMillis());
            Thread.sleep(interval);
        }
    }

    @Override
    public void cancel() {
    }
}
