package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.GuidSignatureBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.GuidIndicators;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class GuidAttributeAgg
        implements AggregateFunction<UbiSession, GuidAttributeAccumulator, GuidAttributeAccumulator> {

    private GuidIndicators guidIndicators;
    private GuidSignatureBotDetector guidSignatureBotDetector;

    @Override
    public GuidAttributeAccumulator createAccumulator() {

        GuidAttributeAccumulator guidAttributeAccumulator = new GuidAttributeAccumulator();
        guidIndicators = GuidIndicators.getInstance();
        guidSignatureBotDetector = GuidSignatureBotDetector.getInstance();

        try {
            guidIndicators.start(guidAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return guidAttributeAccumulator;
    }

    @Override
    public GuidAttributeAccumulator add(UbiSession session, GuidAttributeAccumulator guidAttributeAccumulator) {
        if (guidAttributeAccumulator.getGuidAttribute().getGuid() == null) {
            guidAttributeAccumulator.getGuidAttribute().setGuid(session.getGuid());
        }
        try {
            guidIndicators.feed(session, guidAttributeAccumulator, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Integer> guidBotFlag = null;
        try {
            if (guidAttributeAccumulator.getBotFlagStatus().containsValue(0)
                    || guidAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
                guidBotFlag = guidSignatureBotDetector.getBotFlagList(guidAttributeAccumulator.getGuidAttribute());
                if (guidBotFlag.contains(15)) {
                    switch (guidAttributeAccumulator.getBotFlagStatus().get(15)) {
                        case 0:
                            guidAttributeAccumulator.getBotFlagStatus().put(15,1);
                            break;
                        case 1:
                            guidAttributeAccumulator.getBotFlagStatus().put(15,2);
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
