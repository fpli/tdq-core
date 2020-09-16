package com.ebay.sojourner.rt.operator.attribute;

import com.ebay.sojourner.business.detector.GuidSignatureBotDetector;
import com.ebay.sojourner.business.indicator.GuidIndicators;
import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class GuidAttributeAgg implements
    AggregateFunction<SessionCore, GuidAttributeAccumulator, GuidAttributeAccumulator> {

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

    GuidAttribute guidAttribute = guidAttributeAccumulator.getGuidAttribute();

    if (guidAttribute.getGuid1() == 0 && guidAttribute.getGuid2() == 0) {
      guidAttribute.setGuid1(session.getGuid().getGuid1());
      guidAttribute.setGuid2(session.getGuid().getGuid2());
    }

    try {
      GuidIndicators.getInstance().feed(session, guidAttributeAccumulator);
    } catch (Exception e) {
      log.error("start guid indicators collection failed", e);
    }

    Set<Integer> guidBotFlag = null;
    Map<Integer, Integer> signatureStates = guidAttributeAccumulator.getSignatureStates();

    try {
      if (signatureStates.containsValue(0) || signatureStates.containsValue(1)) {
        guidBotFlag = GuidSignatureBotDetector.getInstance().getBotFlagList(guidAttribute);
        if (guidBotFlag.contains(15)) {
          switch (signatureStates.get(15)) {
            case 0:
              signatureStates.put(15, 1);
              break;
            case 1:
              signatureStates.put(15, 2);
              break;
          }
        }
      }

    } catch (Exception e) {
      log.error("star get guid botFlagList failed", e);
    }

    Set<Integer> botFlagList = SignatureUtils
        .setBotFlags(guidBotFlag, guidAttribute.getBotFlagList());
    guidAttribute.setBotFlagList(botFlagList);
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
