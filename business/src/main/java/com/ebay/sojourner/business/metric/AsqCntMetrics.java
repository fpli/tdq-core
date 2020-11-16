package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import java.util.Map;

public class AsqCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setAsqCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
    Integer pageId = event.getPageId();
    String[] pageFmlyName = pageFmlyNameMap.get(pageId);
    // Jetstream: from SOJEvent(_pgf = 'ASQ' and rdt = 0  and _ifrm = false);
    if (!event.isRdt()
        && !event.isIframe()
        && (pageFmlyName != null && "ASQ".equals(pageFmlyName[1]))) {
      sessionAccumulator
          .getUbiSession()
          .setAsqCnt(sessionAccumulator.getUbiSession().getAsqCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {
  }


}
