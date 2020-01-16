package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;

import java.util.Map;

public class MyebayCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    //TODO extract myebay indicator to external config file
    public static final String[] myEbayIndicator = {"MYEBAY", "SM", "SMP"};
    private static Map<Integer, String[]> pageFmlyNameMap;
    private static LkpFetcher lkpFetcher;

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().setMyebayCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        if (!event.isRdt() && !event.isIframe() && event.isPartialValidPage() && isMyebayPage(event)) {
            sessionAccumulator.getUbiSession().setMyebayCnt(sessionAccumulator.getUbiSession().getMyebayCnt() + 1);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {

    }

    protected boolean isMyebayPage(UbiEvent event) {
        Integer pageId = event.getPageId();
        for (String indicator : myEbayIndicator) {
            String[] pageFmlyNames = pageFmlyNameMap.get(pageId);
            if (pageFmlyNames != null && indicator.equals(pageFmlyNames[1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init() throws Exception {
        lkpFetcher = LkpFetcher.getInstance();
        lkpFetcher.loadPageFmlys();
        pageFmlyNameMap = lkpFetcher.getPageFmlyMaps();
    }
}
