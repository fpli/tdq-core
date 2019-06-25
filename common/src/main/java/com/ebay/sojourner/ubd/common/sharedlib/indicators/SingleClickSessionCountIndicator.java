package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.FieldMetrics;

public class SingleClickSessionCountIndicator implements FieldMetrics<UbiSession, IpAttributeAccumulator> {
    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(IpAttributeAccumulator ipAttributeAccumulator) throws Exception {

    }

    @Override
    public void feed(UbiSession ubiSession, IpAttributeAccumulator ipAttributeAccumulator) throws Exception {

        if (Boolean.TRUE.equals(ubiSession.getSingleClickSessionFlag())) {
            ipAttributeAccumulator.getAttribute().increase();
        }

    }

    @Override
    public void end(IpAttributeAccumulator ipAttributeAccumulator) throws Exception {

    }
}