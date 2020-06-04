package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.GuidSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.GuidIndicators;
import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class GuidAttributeAgg implements
    AggregateFunction<SessionCore,
        GuidAttributeAccumulator, GuidAttributeAccumulator> {

  @Override
  public GuidAttributeAccumulator createAccumulator() {

    GuidAttributeAccumulator guidAttributeAccumulator = new GuidAttributeAccumulator();

    try {
      GuidIndicators.getInstance().start(guidAttributeAccumulator);
    } catch (Exception e) {
      log.error("init guid indicators failed", e);
    }

    return guidAttributeAccumulator;
  }

  @Override
  public GuidAttributeAccumulator add(
      SessionCore session, GuidAttributeAccumulator guidAttributeAccumulator) {
    if (guidAttributeAccumulator.getGuidAttribute().getGuid1() == 0
        && guidAttributeAccumulator.getGuidAttribute().getGuid2() == 0) {
      guidAttributeAccumulator.getGuidAttribute().setGuid1(session.getGuid().getGuid1());
      guidAttributeAccumulator.getGuidAttribute().setGuid2(session.getGuid().getGuid2());
    }
    try {
      GuidIndicators.getInstance().feed(session, guidAttributeAccumulator);
    } catch (Exception e) {
      log.error("start guid indicators collection failed", e);
    }

    Set<Integer> guidBotFlag = null;
    try {
      if (guidAttributeAccumulator.getBotFlagStatus().containsValue(0)
          || guidAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
        guidBotFlag =
            GuidSignatureBotDetector.getInstance()
                .getBotFlagList(guidAttributeAccumulator.getGuidAttribute());
        if (guidBotFlag.contains(15)) {
          switch (guidAttributeAccumulator.getBotFlagStatus().get(15)) {
            case 0:
              guidAttributeAccumulator.getBotFlagStatus().put(15, 1);
              break;
            case 1:
              guidAttributeAccumulator.getBotFlagStatus().put(15, 2);
              break;
          }
        }
      }

    } catch (IOException | InterruptedException e) {
      log.error("star get guid botFlagList failed", e);
    }

    Set<Integer> botFlagList = guidAttributeAccumulator.getGuidAttribute().getBotFlagList();
    if (guidBotFlag != null && guidBotFlag.size() > 0) {
      botFlagList.addAll(guidBotFlag);
    }

    guidAttributeAccumulator.getGuidAttribute().setBotFlagList(botFlagList);

    return guidAttributeAccumulator;
  }

  @Override
  public GuidAttributeAccumulator getResult(GuidAttributeAccumulator guidAttributeAccumulator) {
    return guidAttributeAccumulator;
  }

  @Override
  public GuidAttributeAccumulator merge(GuidAttributeAccumulator a, GuidAttributeAccumulator b) {
    return null;
  }
}
