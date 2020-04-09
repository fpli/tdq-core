package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class ViCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, LkpListener {

  public static final String VI_PAGE_FMLY = "VI";
  private volatile LkpManager lkpManager;
  private  Map<Integer, String[]> pageFmlyNameMap;
  private boolean isContinue;
  @Override
  public void start(SessionAccumulator sessionAccumulator) {

    sessionAccumulator.getUbiSession().setViCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator)
      throws InterruptedException {
    while(!isContinue){
      Thread.sleep(10);
    }
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
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.pageFmly);
    pageFmlyNameMap = lkpManager.getPageFmlyMaps();
    isContinue = true;
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {
    try {
      this.isContinue=false;
      pageFmlyNameMap = lkpManager.getPageFmlyMaps();
      return true;
    } catch (Throwable e) {
      return false;
    }
    finally {
      this.isContinue=true;
    }
  }
}
