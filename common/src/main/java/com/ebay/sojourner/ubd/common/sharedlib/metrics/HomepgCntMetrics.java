package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import java.util.Map;

public class HomepgCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static Map<Integer, String[]> pageFmlyNameMap;
  private static LkpFetcher lkpFetcher;

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {

    sessionAccumulator.getUbiSession().setHomepageCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Integer pageId = event.getPageId();
    if (!event.isRdt()
        && !event.isIframe()
        && event.isPartialValidPage()
        &&
        // no partial valid page condition checked here
        pageFmlyNameMap.containsKey(pageId)
        &&
        // using get(pageId)[1] for page_fmly_3
        "HOME".equals(pageFmlyNameMap.get(pageId)[1])) {
      sessionAccumulator
          .getUbiSession()
          .setHomepageCnt(sessionAccumulator.getUbiSession().getHomepageCnt() + 1);
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
