package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import java.util.Map;

public class ViCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  public static final String VI_PAGE_FMLY = "VI";
  private static LkpFetcher lkpFetcher;
  private static Map<Integer, String[]> pageFmlyNameMap;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {

    sessionAccumulator.getUbiSession().setViCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe() && !event.isRdt()) {
      Integer pageId = event.getPageId();
      String[] pageFmly = pageFmlyNameMap.get(pageId);
      if (pageFmly != null && pageFmly.length > 1 && VI_PAGE_FMLY.equals(pageFmly[1])) {
        sessionAccumulator
            .getUbiSession()
            .setViCnt(sessionAccumulator.getUbiSession().getViCnt() + 1);
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {}

  @Override
  public void init() throws Exception {
    lkpFetcher = LkpFetcher.getInstance();
    lkpFetcher.loadPageFmlys();
    pageFmlyNameMap = lkpFetcher.getPageFmlyMaps();
  }
}
