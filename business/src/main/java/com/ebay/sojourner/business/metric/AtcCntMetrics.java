package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import java.util.Map;

public class AtcCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {


  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setAtcCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
    Integer pageId = event.getPageId();
    String[] pageFmlyName = pageFmlyNameMap.get(pageId);
    if (!event.isRdt()
        && !event.isIframe()
        //  && event.isPartialValidPage()
        && (pageFmlyName != null && "ATC".equals(pageFmlyName[1]))) {
      sessionAccumulator
          .getUbiSession()
          .setAtcCnt(sessionAccumulator.getUbiSession().getAtcCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {

  }

}
