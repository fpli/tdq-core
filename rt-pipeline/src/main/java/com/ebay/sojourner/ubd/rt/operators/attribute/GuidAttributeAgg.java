package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.GuidIndicators;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class GuidAttributeAgg implements AggregateFunction<UbiSession, GuidAttributeAccumulator, GuidAttributeAccumulator> {
    private static final Logger logger = Logger.getLogger(GuidAttributeAgg.class);
    private GuidIndicators guidIndicators;

    @Override
    public GuidAttributeAccumulator createAccumulator() {

        GuidAttributeAccumulator guidAttributeAccumulator = new GuidAttributeAccumulator();
        guidIndicators = GuidIndicators.getInstance();

        try {
            guidIndicators.start(guidAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return guidAttributeAccumulator;
    }

    @Override
    public GuidAttributeAccumulator add(UbiSession session, GuidAttributeAccumulator guidAttributeAccumulator) {
        if (guidAttributeAccumulator.getGuidAttribute().getGuid() == null) {
            guidAttributeAccumulator.getGuidAttribute().setGuid(session.getGuid());
        }
        try {
            guidIndicators.feed(session,guidAttributeAccumulator,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
