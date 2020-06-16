package com.ebay.sojourner.rt.operators.event;

import com.ebay.sojourner.business.ubd.detectors.AbstractBotDetector;
import com.ebay.sojourner.business.ubd.detectors.BotDetectorFactory;
import com.ebay.sojourner.business.ubd.detectors.BotDetectorFactory.Type;
import com.ebay.sojourner.business.ubd.parser.EventParser;
import com.ebay.sojourner.business.ubd.rule.RuleManager;
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
  private AbstractBotDetector<UbiEvent> eventBotDetector;
  private AverageAccumulator avgEventParserDuration = new AverageAccumulator();
  private AverageAccumulator avgBotDetectionDuration = new AverageAccumulator();

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    parser = new EventParser();
    eventBotDetector = BotDetectorFactory.get(Type.EVENT, RuleManager.getInstance());

    getRuntimeContext()
        .addAccumulator("Average Duration of Event Parsing", avgEventParserDuration);
    getRuntimeContext()
        .addAccumulator("Average Duration of Event BotDetection", avgBotDetectionDuration);
  }

  @Override
  public UbiEvent map(RawEvent rawEvent) throws Exception {
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
    super.close();
    // RuleManager.getInstance().close();
  }
}
