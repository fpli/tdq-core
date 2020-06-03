package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.business.ubd.detectors.AbstractBotDetector;
import com.ebay.sojourner.business.ubd.detectors.BotDetectorFactory;
import com.ebay.sojourner.business.ubd.detectors.BotDetectorFactory.Type;
import com.ebay.sojourner.business.ubd.parser.AgentInfoParser;
import com.ebay.sojourner.business.ubd.parser.AppIdParser;
import com.ebay.sojourner.business.ubd.parser.CiidParser;
import com.ebay.sojourner.business.ubd.parser.ClickIdParser;
import com.ebay.sojourner.business.ubd.parser.ClientIPParser;
import com.ebay.sojourner.business.ubd.parser.CobrandParser;
import com.ebay.sojourner.business.ubd.parser.CookiesParser;
import com.ebay.sojourner.business.ubd.parser.EventParser;
import com.ebay.sojourner.business.ubd.parser.FindingFlagParser;
import com.ebay.sojourner.business.ubd.parser.FlagsParser;
import com.ebay.sojourner.business.ubd.parser.IFrameParser;
import com.ebay.sojourner.business.ubd.parser.IcfParser;
import com.ebay.sojourner.business.ubd.parser.IdentityParser;
import com.ebay.sojourner.business.ubd.parser.ItemIdParser;
import com.ebay.sojourner.business.ubd.parser.JSColumnParser;
import com.ebay.sojourner.business.ubd.parser.PageIdParser;
import com.ebay.sojourner.business.ubd.parser.PartialValidPageParser;
import com.ebay.sojourner.business.ubd.parser.RdtParser;
import com.ebay.sojourner.business.ubd.parser.RefererParser;
import com.ebay.sojourner.business.ubd.parser.ReferrerHashParser;
import com.ebay.sojourner.business.ubd.parser.ReguParser;
import com.ebay.sojourner.business.ubd.parser.ServerParser;
import com.ebay.sojourner.business.ubd.parser.SiidParser;
import com.ebay.sojourner.business.ubd.parser.SiteIdParser;
import com.ebay.sojourner.business.ubd.parser.SqrParser;
import com.ebay.sojourner.business.ubd.parser.StaticPageTypeParser;
import com.ebay.sojourner.business.ubd.parser.TimestampParser;
import com.ebay.sojourner.business.ubd.parser.UserIdParser;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class EventMapFunction extends RichMapFunction<RawEvent, UbiEvent> {

  private EventParser parser;
  private AbstractBotDetector<UbiEvent> eventBotDetector;
  private AverageAccumulator avgEventParserDuration = new AverageAccumulator();
  private Map<String, AverageAccumulator> eventParseMap = new ConcurrentHashMap<>();
  private AverageAccumulator avgBotDetectionDuration = new AverageAccumulator();

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    parser = new EventParser();
    eventBotDetector = BotDetectorFactory.get(Type.EVENT);
    RuleManager.getInstance().addListener(eventBotDetector);

    getRuntimeContext()
        .addAccumulator("Average Duration of Event Parsing", avgEventParserDuration);

    getRuntimeContext()
        .addAccumulator("Average Duration of Event BotDetection", avgBotDetectionDuration);

    List<String> eventParserNames =
        Arrays.asList(
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
            UserIdParser.class.getSimpleName(),
            IcfParser.class.getSimpleName(),
            JSColumnParser.class.getSimpleName());

    for (String className : eventParserNames) {
      AverageAccumulator accumulator = new AverageAccumulator();
      eventParseMap.put(className, accumulator);
      log.info("Add accumulator for {}", className);
      getRuntimeContext()
          .addAccumulator(String.format("Average Duration of %s", className), accumulator);
    }
  }

  @Override
  public UbiEvent map(RawEvent rawEvent) throws Exception {
    UbiEvent event = new UbiEvent();
    long startTimeForEventParser = System.nanoTime();
    //parser.parse(rawEvent, event, eventParseMap);
    //TODO(Jason/Haibo): refactor this
    parser.parse(rawEvent, event);
    avgEventParserDuration.add(System.nanoTime() - startTimeForEventParser);
    long startTimeForEventBotDetection = System.nanoTime();
    Set<Integer> botFlagList = eventBotDetector.getBotFlagList(event);
    avgBotDetectionDuration.add(System.nanoTime() - startTimeForEventBotDetection);
    event.getBotFlags().addAll(botFlagList);
    return event;
  }

  @Override
  public void close() throws Exception {
    eventBotDetector.close();
    super.close();
  }
}
