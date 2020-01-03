package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.parser.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EventMapFunction extends RichMapFunction<RawEvent, UbiEvent> {
    private EventParser parser;
    private EventBotDetector eventBotDetector;
    private AverageAccumulator avgDuration = new AverageAccumulator();
    private Map<String, AverageAccumulator> eventParseMap = new ConcurrentHashMap<>();

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);

        parser = new EventParser();
        eventBotDetector = EventBotDetector.getInstance();

//        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        getRuntimeContext().addAccumulator("Average Duration of Event Parsing", avgDuration);

        List<String> classNames = Arrays.asList(
                AgentInfoParser.class.getSimpleName(),
                AppIdParser.class.getSimpleName(),
                CiidParser.class.getSimpleName(),
                ClickIdParser.class.getSimpleName(),
                ClientIPParser.class.getSimpleName(),
                CobrandParser.class.getSimpleName(),
                CookiesParser.class.getSimpleName(),
                FindingFlagParser.class.getSimpleName(),
                FlagsParser.class.getSimpleName(),
                IdentityParser.class.getSimpleName(),
                IFrameParser.class.getSimpleName(),
                ItemIdParser.class.getSimpleName(),
                PageIdParser.class.getSimpleName(),
                PartialValidPageParser.class.getSimpleName(),
                RdtParser.class.getSimpleName(),
                RefererParser.class.getSimpleName(),
                ReferrerHashParser.class.getSimpleName(),
                ReguParser.class.getSimpleName(),
                ServerParser.class.getSimpleName(),
                SiidParser.class.getSimpleName(),
                SiteIdParser.class.getSimpleName(),
                SqrParser.class.getSimpleName(),
                StaticPageTypeParser.class.getSimpleName(),
                TimestampParser.class.getSimpleName(),
                UserIdParser.class.getSimpleName()
        );


        for (String className : classNames) {
            AverageAccumulator accumulator = new AverageAccumulator();
            eventParseMap.put(className, accumulator);
            log.info("Add accumulator for {}", className);
            getRuntimeContext().addAccumulator(String.format("Average Duration of %s", className), accumulator);
        }
    }

    @Override
    public UbiEvent map(RawEvent rawEvent) throws Exception {
        UbiEvent event = new UbiEvent();
        long startTime = System.nanoTime();
        parser.parse(rawEvent, event, eventParseMap);
        avgDuration.add(System.nanoTime() - startTime);
        event.getBotFlags().addAll(eventBotDetector.getBotFlagList(event));
        return event;
    }
}