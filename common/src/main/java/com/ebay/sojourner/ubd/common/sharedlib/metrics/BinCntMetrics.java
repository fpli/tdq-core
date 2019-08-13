package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.FlagUtils;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;

public class BinCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private PageIndicator indicator;
    private static UBIConfig ubiConfig;

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setBinCoreCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (event.getIframe() == 0 && event.getRdt() == 0 &&
                indicator.isCorrespondingPageEvent(event) &&
                (FlagUtils.matchFlag(event, 6, 1) || FlagUtils.matchFlag(event, 48, 1))) {
            sessionAccumulator.getUbiSession().setBinCoreCnt(sessionAccumulator.getUbiSession().getBinCoreCnt() + 1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void init() throws Exception {
        InputStream resourceAsStream = BinCntMetrics.class.getResourceAsStream("/ubi.properties");
        ubiConfig = UBIConfig.getInstance(resourceAsStream);
        setPageIndicator(new PageIndicator(ubiConfig.getString(Property.BIN_PAGES)));
    }

    void setPageIndicator(PageIndicator indicator) {
        this.indicator = indicator;
    }
}
