package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.util.Set;

public class WatchCntMetric implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private Set<Integer> searchViewPageSet = null;
    private static UBIConfig ubiConfig;
    @Override
    public void start(SessionAccumulator sessionAccumulator) {

        sessionAccumulator.getUbiSession().setWatchCoreCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (event.getIframe() == 0 && 
                searchViewPageSet.contains(event.getPageId()) &&
                event.getItemId() != null) {
            sessionAccumulator.getUbiSession().setWatchCoreCnt(sessionAccumulator.getUbiSession().getWatchCoreCnt()+1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void init() throws Exception {
        ubiConfig = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties"));
        searchViewPageSet = PageIndicator.parse(ubiConfig.getString(Property.SEARCH_VIEW_PAGES));
    }
}
