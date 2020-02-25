package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Map;

public class GrCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    private ArrayList<String> viPGT;
    private Map<Integer, String[]> pageFmlyNameMap;
    private LkpFetcher lkpFetcher;

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().setGrCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        Integer pageId = event.getPageId();
        if (!event.isRdt() && !event.isIframe() && event.isPartialValidPage() && pageId != -1 &&
                ((pageFmlyNameMap.containsKey(pageId) && "GR".equals(pageFmlyNameMap.get(pageId)[1])) || (getImPGT(event) != null && "GR".equals(getImPGT(event))))) {
            sessionAccumulator.getUbiSession().setGrCnt(sessionAccumulator.getUbiSession().getGrCnt() + 1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {

    }

    @Override
    public void init() throws Exception {
        lkpFetcher = LkpFetcher.getInstance();
        lkpFetcher.loadPageFmlys();
        pageFmlyNameMap = lkpFetcher.getPageFmlyMaps();
        viPGT = new ArrayList<>(PropertyUtils.parseProperty(UBIConfig.getString(Property.VI_EVENT_VALUES), Property.PROPERTY_DELIMITER));
    }

    private String getImPGT(UbiEvent event) {
        if (event.getPageId() == 1521826 && StringUtils.isNotBlank(SOJNVL.getTagValue(event.getApplicationPayload(), "pgt")) && viPGT.contains(SOJNVL.getTagValue(event.getApplicationPayload(), "pgt"))) {
            return "VI";
        }
        if (event.getPageId() == 2066804 && StringUtils.isNotBlank(event.getUrlQueryString()) && (event.getUrlQueryString().startsWith("/itm/like") || event.getUrlQueryString().startsWith("/itm/future"))) {
            return "VI";
        }
        if (event.getPageId() == 1521826 || event.getPageId() == 2066804) {
            return "GR";
        }
        return null;
    }

}
