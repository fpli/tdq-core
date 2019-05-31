package com.ebay.sojourner.ubd.rt.operators.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.EventParser;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.io.File;

public class EventParserMapFunction extends RichMapFunction<RawEvent,UbiEvent> {
    private EventParser parser;
    private AverageAccumulator avgDuration = new AverageAccumulator();
    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        File configFile = getRuntimeContext().getDistributedCache().getFile("configFile");
        UBIConfig ubiConfig = UBIConfig.getInstance(configFile);

        parser = new EventParser();
        getRuntimeContext().addAccumulator("Average Duration of Event Parsing", avgDuration);

    }

    @Override
    public UbiEvent map(RawEvent rawEvent) throws Exception {
        UbiEvent event = new UbiEvent();
        long startTime = System.nanoTime();
        parser.parse(rawEvent, event);
        avgDuration.add(System.nanoTime() - startTime);
       return event;
    }


}