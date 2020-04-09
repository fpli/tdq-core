package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class LogdnCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, LkpListener {

  private static final String SIGN_IN = "SIGNIN";
  private Map<Integer, String[]> pageFmlyNameMap;
  private volatile LkpManager lkpManager;
  private boolean isContinue;
  public static void main(String[] args) {
  //    Map<Integer, String[]> pageFmlyNameMap2;
  //    LkpManager lkpFetcher2;
  //    lkpFetcher2 = LkpManager.getInstance();
  //    lkpFetcher2.loadPageFmlys();
  //    pageFmlyNameMap2 = lkpFetcher2.getPageFmlyMaps();
  //    if (pageFmlyNameMap2.containsKey(992)) {
  //      System.out.println(pageFmlyNameMap2.get(992)[1]);
  //    }

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setSigninPageCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    while(!isContinue){
      Thread.sleep(10);
    }
    Integer pageId = event.getPageId();

    if (!event.isRdt()
        && !event.isIframe()
        && event.isPartialValidPage()
        &&
        // no partial valid page condition checked here
        pageFmlyNameMap.containsKey(pageId)
        &&
        // using get(pageId)[1] for page_fmly_3
        SIGN_IN.equals(pageFmlyNameMap.get(pageId)[1])) {
      sessionAccumulator
          .getUbiSession()
          .setSigninPageCnt(sessionAccumulator.getUbiSession().getSigninPageCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.pageFmly);
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
