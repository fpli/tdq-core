package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LkpFetcher;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class FindingFlagParser implements FieldParser<RawEvent, UbiEvent> {

  private static LkpFetcher lkpFetcher;

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Integer pageId = ubiEvent.getPageId();
    Map<Integer, Integer> findingFlagMap = LkpManager.getInstance().getFindingFlagMap();
    if (findingFlagMap.containsKey(pageId)) {
      ubiEvent.setBitVal(findingFlagMap.get(pageId));
    }
  }

  @Override
  public void init() throws Exception {
  }
}
