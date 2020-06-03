package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import java.util.Map;

public class MyebayCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  // TODO extract myebay indicator to external config file
  public static final String[] myEbayIndicator = {"MYEBAY", "SM", "SMP"};

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setMyebayCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
    if (!event.isRdt() && !event.isIframe() && event.isPartialValidPage() && isMyebayPage(event,
        pageFmlyNameMap)) {
      sessionAccumulator
          .getUbiSession()
          .setMyebayCnt(sessionAccumulator.getUbiSession().getMyebayCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  protected boolean isMyebayPage(UbiEvent event, Map<Integer, String[]> pageFmlyNameMap) {
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

  }

}
