package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

public class LndgPageIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private Integer minSCSeqNum;
    private Integer lndgPageId;
    private Set<Integer> invalidPageIds;
    private static UBIConfig ubiConfig;

    @Override
    public void init() throws Exception {
        InputStream resourceAsStream = LndgPageIdMetrics.class.getResourceAsStream("/ubi.properties");
        ubiConfig = UBIConfig.getInstance(resourceAsStream);
        invalidPageIds = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.INVALID_PAGE_IDS), Property.PROPERTY_DELIMITER);
    }

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().setMinSCSeqNum(Integer.MAX_VALUE);
        sessionAccumulator.getUbiSession().setLndgPageId(Integer.MIN_VALUE);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        if (event.getIframe() == 0 && event.getRdt() == 0 && !invalidPageIds.contains(event.getPageId())) {
            if (sessionAccumulator.getUbiSession().getMinSCSeqNum() > event.getSeqNum()) {
                sessionAccumulator.getUbiSession().setMinSCSeqNum(event.getSeqNum());
                sessionAccumulator.getUbiSession().setLndgPageId(event.getPageId());
            }
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {
    }

}
