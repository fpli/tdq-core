package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class AbsEventCountIndicator implements Indicator<UbiSession, GuidAttributeAccumulator> {
    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {
        guidAttributeAccumulator.getAttribute().clear();
    }

    @Override
    public void feed(UbiSession ubiSession, GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {

        guidAttributeAccumulator.getAttribute().feed(ubiSession, BotRules.MANY_EVENTS_BOT_FLAG);

    }

    @Override
    public void end(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {

    }

    @Override
    public boolean filter(UbiSession ubiSession, GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {

        return false;
    }
}