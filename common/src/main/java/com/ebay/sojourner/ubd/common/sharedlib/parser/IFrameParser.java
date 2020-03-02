package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class IFrameParser implements FieldParser<RawEvent, UbiEvent> {

  private static final String P_TAG = "p";
  private static LkpFetcher lkpFetcher;

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {

    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());
    String pageId = null;
    if (StringUtils.isNotBlank(map.get(P_TAG))) {
      pageId = map.get(P_TAG);
    }

    Set<String> pageIdSet = LkpFetcher.getInstance().getIframePageIdSet();

    if (pageIdSet.contains(pageId)) {
      ubiEvent.setIframe(true);
    } else {
      ubiEvent.setIframe(false);
    }
  }

  @Override
  public void init() throws Exception {}
}
