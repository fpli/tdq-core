package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.GuidSignatureBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.GuidIndicators;
import com.ebay.sojourner.ubd.common.util.Constants;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class GuidAttributeAgg implements
    AggregateFunction<IntermediateSession,
        GuidAttributeAccumulator, GuidAttributeAccumulator> {

  private static final String GUID = Constants.GUID_LEVEL;
  // private GuidIndicators guidIndicators;
  // private GuidSignatureBotDetector guidSignatureBotDetector;
  // private RuleManager ruleManager;

  @Override
  public GuidAttributeAccumulator createAccumulator() {

    GuidAttributeAccumulator guidAttributeAccumulator = new GuidAttributeAccumulator();
    // guidIndicators = GuidIndicators.getInstance();
    // guidSignatureBotDetector = GuidSignatureBotDetector.getInstance();
    // ruleManager = RuleManager.getInstance();

    try {
      GuidIndicators.getInstance().start(guidAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return guidAttributeAccumulator;
  }

  @Override
  public GuidAttributeAccumulator add(
      IntermediateSession session, GuidAttributeAccumulator guidAttributeAccumulator) {
    if (guidAttributeAccumulator.getGuidAttribute().getGuid1() == 0
        && guidAttributeAccumulator.getGuidAttribute().getGuid2() == 0) {
      guidAttributeAccumulator.getGuidAttribute().setGuid1(session.getGuid1());
      guidAttributeAccumulator.getGuidAttribute().setGuid2(session.getGuid2());
    }
    try {
      GuidIndicators.getInstance().feed(session, guidAttributeAccumulator, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Set<Integer> guidBotFlag = null;
    try {
      if (guidAttributeAccumulator.getBotFlagStatus().containsValue(0)
          || guidAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
        /*
        guidSignatureBotDetector.initDynamicRules(ruleManager, guidSignatureBotDetector.rules(),
            GuidSignatureBotDetector.dynamicRuleIdList(), GUID);
            */
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
      log.error("guid getBotFlagList error", e);
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
