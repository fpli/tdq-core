package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Set;

public class IFrameParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {

    int pageId = ubiEvent.getPageId();

    Set<Integer> pageIdSet = LkpManager.getInstance().getIframePageIdSet();

    if (pageIdSet.contains(pageId)) {
      ubiEvent.setIframe(true);
    } else {
      ubiEvent.setIframe(false);
    }
  }

  @Override
  public void init() throws Exception {
  }
}
