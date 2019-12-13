package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.parser.*;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EventMapFunction extends RichMapFunction<RawEvent,UbiEvent> {
    private EventParser parser;
    private EventBotDetector eventBotDetector;

    // new accumulator
    private AverageAccumulator avgDuration = new AverageAccumulator();
    private AverageAccumulator agentInfoDuration = new AverageAccumulator();
    private AverageAccumulator appIdDuration = new AverageAccumulator();
    private AverageAccumulator ciidDuration = new AverageAccumulator();
    private AverageAccumulator clickIdDuration = new AverageAccumulator();
    private AverageAccumulator clientIpDuration = new AverageAccumulator();
    private AverageAccumulator cookiesDuration = new AverageAccumulator();
    private AverageAccumulator cobrandDuration = new AverageAccumulator();
    private AverageAccumulator findingFlagDuration = new AverageAccumulator();
    private AverageAccumulator flagsDuration = new AverageAccumulator();
    private AverageAccumulator identityDuration = new AverageAccumulator();
    private AverageAccumulator itemIdDuration = new AverageAccumulator();
    private AverageAccumulator iFrameDuration = new AverageAccumulator();
    private AverageAccumulator pageIdDuration = new AverageAccumulator();
    private AverageAccumulator partialValidPageDuration = new AverageAccumulator();
    private AverageAccumulator rdtDuration = new AverageAccumulator();
    private AverageAccumulator refererDuration = new AverageAccumulator();
    private AverageAccumulator referrerHashDuration = new AverageAccumulator();
    private AverageAccumulator reguDuration = new AverageAccumulator();
    private AverageAccumulator serverDuration = new AverageAccumulator();
    private AverageAccumulator siidDuration = new AverageAccumulator();
    private AverageAccumulator siteIdDuration = new AverageAccumulator();
    private AverageAccumulator sqrDuration = new AverageAccumulator();
    private AverageAccumulator staticPageTypeDuration = new AverageAccumulator();
    private AverageAccumulator timeStampDuration = new AverageAccumulator();
    private AverageAccumulator userIdDuration = new AverageAccumulator();

    private Map<String,AverageAccumulator> parserMap = new HashMap<>();

    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
//        InputStream configFile = getRuntimeContext().getDistributedCache().getClass().getResourceAsStream("configFile");
//        UBIConfig ubiConfig = UBIConfig.getInstance(configFile);

        parser = new EventParser();
        eventBotDetector = EventBotDetector.getInstance();

        // add accumulator
        getRuntimeContext().addAccumulator("Average Duration of Event Parsing", avgDuration);

        getRuntimeContext().addAccumulator("Average Duration of agentInfo field Parsing",agentInfoDuration);
        getRuntimeContext().addAccumulator("Average Duration of appId field Parsing", appIdDuration);
        getRuntimeContext().addAccumulator("Average Duration of ciid field Parsing", ciidDuration);
        getRuntimeContext().addAccumulator("Average Duration of clickId field Parsing",clickIdDuration);
        getRuntimeContext().addAccumulator("Average Duration of clientID field Parsing",clientIpDuration);
        getRuntimeContext().addAccumulator("Average Duration of cobrand field Parsing",cobrandDuration);
        getRuntimeContext().addAccumulator("Average Duration of cookies field Parsing",cookiesDuration);
        getRuntimeContext().addAccumulator("Average Duration of findingFlag field Parsing",findingFlagDuration);
        getRuntimeContext().addAccumulator("Average Duration of flags field Parsing",flagsDuration);
        getRuntimeContext().addAccumulator("Average Duration of identity field Parsing",identityDuration);
        getRuntimeContext().addAccumulator("Average Duration of iFrame field Parsing",iFrameDuration);
        getRuntimeContext().addAccumulator("Average Duration of pageId field Parsing",pageIdDuration);
        getRuntimeContext().addAccumulator("Average Duration of partialValidPage field Parsing",partialValidPageDuration);
        getRuntimeContext().addAccumulator("Average Duration of rdt field Parsing",rdtDuration);
        getRuntimeContext().addAccumulator("Average Duration of referer field Parsing",refererDuration);
        getRuntimeContext().addAccumulator("Average Duration of referrerHash field Parsing",referrerHashDuration);
        getRuntimeContext().addAccumulator("Average Duration of regu field Parsing",reguDuration);
        getRuntimeContext().addAccumulator("Average Duration of server field Parsing",serverDuration);
        getRuntimeContext().addAccumulator("Average Duration of siid field Parsing",siidDuration);
        getRuntimeContext().addAccumulator("Average Duration of siteId field Parsing",siteIdDuration);
        getRuntimeContext().addAccumulator("Average Duration of sqr field Parsing",sqrDuration);
        getRuntimeContext().addAccumulator("Average Duration of staticPageType field Parsing",staticPageTypeDuration);
        getRuntimeContext().addAccumulator("Average Duration of timeStamp field Parsing",timeStampDuration);
        getRuntimeContext().addAccumulator("Average Duration of userId field Parsing",userIdDuration);
        getRuntimeContext().addAccumulator("Average Duration of itemId field Parsing",itemIdDuration);

        parserMap.put(AgentInfoParser.class.getSimpleName(),agentInfoDuration);
        parserMap.put(AppIdParser.class.getSimpleName(),appIdDuration);
        parserMap.put(CiidParser.class.getSimpleName(),ciidDuration);
        parserMap.put(ClickIdParser.class.getSimpleName(),clickIdDuration);
        parserMap.put(ClientIPParser.class.getSimpleName(),clientIpDuration);
        parserMap.put(CobrandParser.class.getSimpleName(),cobrandDuration);
        parserMap.put(CookiesParser.class.getSimpleName(),cookiesDuration);
        parserMap.put(FindingFlagParser.class.getSimpleName(),findingFlagDuration);
        parserMap.put(FlagsParser.class.getSimpleName(),flagsDuration);
        parserMap.put(IdentityParser.class.getSimpleName(),identityDuration);
        parserMap.put(IFrameParser.class.getSimpleName(),iFrameDuration);
        parserMap.put(ItemIdParser.class.getSimpleName(),itemIdDuration);
        parserMap.put(PageIdParser.class.getSimpleName(),pageIdDuration);
        parserMap.put(PartialValidPageParser.class.getSimpleName(),partialValidPageDuration);
        parserMap.put(RdtParser.class.getSimpleName(),rdtDuration);
        parserMap.put(RefererParser.class.getSimpleName(),refererDuration);
        parserMap.put(ReferrerHashParser.class.getSimpleName(),referrerHashDuration);
        parserMap.put(ReguParser.class.getSimpleName(),reguDuration);
        parserMap.put(ServerParser.class.getSimpleName(),serverDuration);
        parserMap.put(SiidParser.class.getSimpleName(),siidDuration);
        parserMap.put(SiteIdParser.class.getSimpleName(),siteIdDuration);
        parserMap.put(SqrParser.class.getSimpleName(),sqrDuration);
        parserMap.put(StaticPageTypeParser.class.getSimpleName(),staticPageTypeDuration);
        parserMap.put(TimestampParser.class.getSimpleName(),timeStampDuration);
        parserMap.put(UserIdParser.class.getSimpleName(),userIdDuration);
    }

    @Override
    public UbiEvent map(RawEvent rawEvent) throws Exception {

        UbiEvent event = new UbiEvent();

        long startTime = System.nanoTime();
        parser.parse(rawEvent, event,parserMap);
        avgDuration.add(System.nanoTime() - startTime);
        event.getBotFlags().addAll(eventBotDetector.getBotFlagList(event));
        return event;
    }
}