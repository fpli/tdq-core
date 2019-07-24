package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.util.Set;

public class SiidCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    private static Set<Integer> cobrandSets;
    private static UBIConfig ubiConfig;
    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setSiidCnt(0);
        sessionAccumulator.getUbiSession().setSiidCnt2(0);
        sessionAccumulator.getUbiSession().setRefererNull(true);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (event.getIframe() == 0 && event.getRdt() == 0 && event.getSourceImprId() != null) {
            sessionAccumulator.getUbiSession().setSiidCnt(sessionAccumulator.getUbiSession().getSiidCnt() + 1);
        }
        if (event.getIframe() == 0 && event.getRdt() == 0 && sessionAccumulator.getUbiSession().isRefererNull()) {
            if (event.getSourceImprId() != null) {
                sessionAccumulator.getUbiSession().setSiidCnt2(sessionAccumulator.getUbiSession().getSiidCnt2()+1);
            }
            if (event.getReferrer() != null) {
                sessionAccumulator.getUbiSession().setRefererNull(false);
            }
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void init() throws Exception {

    }
}
