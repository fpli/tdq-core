package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import java.util.Set;

public class IFrameParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Set<Integer> pageIdSet = LkpManager.getInstance().getIframePageIdSet();
    int pageId = ubiEvent.getPageId();
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
