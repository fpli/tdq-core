package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Set;

public class IFrameParser implements FieldParser<RawEvent, UbiEvent>, LkpListener {

  private volatile LkpManager lkpManager;
  private boolean isContinue;
  private Set<Integer> pageIdSet;

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    while (!isContinue) {
      Thread.sleep(10);
    }
    int pageId = ubiEvent.getPageId();
    if (pageIdSet.contains(pageId)) {
      ubiEvent.setIframe(true);
    } else {
      ubiEvent.setIframe(false);
    }
  }

  @Override
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.iframePageIds);
    isContinue = true;
    pageIdSet = lkpManager.getIframePageIdSet();
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {
    try {
      this.isContinue = false;
      pageIdSet = lkpManager.getIframePageIdSet();
      return true;
    } catch (Throwable e) {
      return false;
    } finally {
      this.isContinue = true;
    }
  }
}
