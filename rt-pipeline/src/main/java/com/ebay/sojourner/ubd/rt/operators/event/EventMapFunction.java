package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.parser.EventParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class EventMapFunction extends RichMapFunction<RawEvent,UbiEvent> {
    private EventParser parser;
    private EventBotDetector eventBotDetector;
    private AverageAccumulator avgDuration = new AverageAccumulator();
//    private Map<String,AverageAccumulator> eventParseMap = new HashMap<>();

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);

        parser = new EventParser();
        eventBotDetector = EventBotDetector.getInstance();

//        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        getRuntimeContext().addAccumulator("Average Duration of Event Parsing", avgDuration);

//        for (ClassPath.ClassInfo className : ClassPath.from(EventMapFunction.class.getClassLoader()).getTopLevelClasses("com.ebay.sojourner.ubd.common.sharedlib.parser")) {
//            eventParseMap.put(className.getSimpleName(),new AverageAccumulator());
//            getRuntimeContext().addAccumulator(String.format("Average Duration of %s", className.getSimpleName()), eventParseMap.get(className.getSimpleName()));
//        }
    }

    @Override
    public UbiEvent map(RawEvent rawEvent) throws Exception {
        UbiEvent event = new UbiEvent();
//        long startTime = System.nanoTime();
        parser.parse(rawEvent,event);
//        avgDuration.add(System.nanoTime() - startTime);
        event.getBotFlags().addAll(eventBotDetector.getBotFlagList(event));
        return event;
    }
}