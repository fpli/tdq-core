package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetPageType;
import java.util.Map;
import org.apache.log4j.Logger;

public class StaticPageTypeParser implements FieldParser<RawEvent, UbiEvent> {
  private static final Logger log = Logger.getLogger(StaticPageTypeParser.class);
  private static LkpFetcher lkpFetcher;

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    int result = 0;
    try {
      Long itemId = ubiEvent.getItemId();
      String flags = ubiEvent.getFlags();
      Integer pageId = ubiEvent.getPageId();
      Integer[] pageInfo = new Integer[2];
      Map<Integer, Integer[]> vtNewIdsMap = LkpFetcher.getInstance().getVtNewIdsMap();
      if (pageId != null) {
        if (vtNewIdsMap.containsKey(pageId)) {
          pageInfo = vtNewIdsMap.get(pageId);
        }
        String itemid;
        if (itemId == null) {
          itemid = null;
        } else {
          itemid = String.valueOf(itemId);
        }
        result =
            SOJGetPageType.soj_get_page_type(
                itemid, flags, ubiEvent.isRdt(), pageId.intValue(), pageInfo[0], pageInfo[1]);
      } else {
        result = -1;
      }
      ubiEvent.setStaticPageType(result);
    } catch (Exception e) {
      log.error("Parse static page type error: " + e.getMessage());
    }
  }

  @Override
  public void init() throws Exception {}
}
