package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;

import java.util.Map;

public class LogdnCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    public static final String SIGN_IN = "SIGNIN";
    private static Map<Integer, String[]> pageFmlyNameMap;
    private static LkpFetcher lkpFetcher;

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        sessionAccumulator.getUbiSession().setSigninPageCnt(0);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        Integer pageId = event.getPageId();

        if (event.getRdt() != Integer.MIN_VALUE && event.getRdt() == 0 && (event.getIframe() == Integer.MIN_VALUE || event.getIframe() == 0) && event.getPartialValidPage() != Integer.MIN_VALUE && event.getPartialValidPage() == 1 &&
                //no partial valid page condition checked here
                pageFmlyNameMap.containsKey(pageId) &&
                //using get(pageId)[1] for page_fmly_3
                SIGN_IN.equals(pageFmlyNameMap.get(pageId)[1])) {
            sessionAccumulator.getUbiSession().setSigninPageCnt(sessionAccumulator.getUbiSession().getSigninPageCnt() + 1);
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
    }
}