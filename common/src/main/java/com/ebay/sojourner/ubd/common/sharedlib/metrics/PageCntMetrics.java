package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;

public class PageCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private PageIndicator indicator;

    private static UBIConfig ubiConfig;

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setPageCnt(0);


    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        if (indicator.isCorrespondingPageEvent(event)) {
            sessionAccumulator.getUbiSession().setPageCnt(sessionAccumulator.getUbiSession().getPageCnt() + 1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {

    }

    @Override
    public void init() throws Exception {
        ubiConfig = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties"));
        setPageIndicator(new PageIndicator(ubiConfig.getString(Property.CAPTCHA_PAGES)));
    }

    void setPageIndicator(PageIndicator indicator) {
        this.indicator = indicator;
    }
}
