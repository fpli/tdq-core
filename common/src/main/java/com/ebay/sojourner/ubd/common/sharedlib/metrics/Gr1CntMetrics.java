package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class Gr1CntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, LkpListener {

  private volatile LkpManager lkpManager;
  private Map<Integer, String[]> pageFmlyNameMap;
  private boolean isContinue;
  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setGr1Cnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    while(!isContinue){
      Thread.sleep(10);
    }
    Integer pageId = event.getPageId();
    String[] pageFmlyName = pageFmlyNameMap.get(pageId);
    if (!event.isRdt()
        && !event.isIframe()
        && event.isPartialValidPage()
        && (pageFmlyName != null && "GR-1".equals(pageFmlyName[1]))) {
      sessionAccumulator
          .getUbiSession()
          .setGr1Cnt(sessionAccumulator.getUbiSession().getGr1Cnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {
    lkpManager =  new LkpManager(this, LkpEnum.pageFmly);
    pageFmlyNameMap = lkpManager.getPageFmlyMaps();
    isContinue=true;
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
