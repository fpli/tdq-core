package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class HomepgCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {

    sessionAccumulator.getUbiSession().setHomepageCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
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

  }

}
