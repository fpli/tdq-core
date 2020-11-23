package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.business.detector.EventBotDetector;
import com.ebay.sojourner.business.parser.EventParser;
import com.ebay.sojourner.dsl.domain.RuleManager;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class EventMapFunction extends RichMapFunction<RawEvent, UbiEvent> {

  private EventParser parser;
  private EventBotDetector eventBotDetector;
  private AverageAccumulator avgEventParserDuration = new AverageAccumulator();
  private AverageAccumulator avgBotDetectionDuration = new AverageAccumulator();

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    parser = new EventParser();
    eventBotDetector = new EventBotDetector();

    getRuntimeContext()
        .addAccumulator("Average Duration of Event Parsing", avgEventParserDuration);
    getRuntimeContext()
        .addAccumulator("Average Duration of Event BotDetection", avgBotDetectionDuration);
  }

  @Override
  public UbiEvent map(RawEvent rawEvent) throws Exception {

    if (rawEvent != null) {
      throw new NullPointerException("test metaspace leak for flink 1.11");
    }
    UbiEvent event = new UbiEvent();
    long startTimeForEventParser = System.nanoTime();
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
    RuleManager.getInstance().close();
    super.close();
  }
}
