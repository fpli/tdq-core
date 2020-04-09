package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJGetPageType;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticPageTypeParser implements FieldParser<RawEvent, UbiEvent>, LkpListener {

  private static final Logger log = LoggerFactory.getLogger(StaticPageTypeParser.class);
  private volatile LkpManager lkpManager;
  private boolean isContinue;
  private Map<Integer, Integer[]> vtNewIdsMap;

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    while (!isContinue) {
      Thread.sleep(10);
    }
    int result = 0;
    try {
      Long itemId = ubiEvent.getItemId();
      String flags = ubiEvent.getFlags();
      Integer pageId = ubiEvent.getPageId();
      Integer[] pageInfo = new Integer[2];
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
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.pageFmly);
    vtNewIdsMap = lkpManager.getVtNewIdsMap();
    isContinue = true;
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {
    try {
      this.isContinue = false;
      vtNewIdsMap = lkpManager.getVtNewIdsMap();
      return true;
    } catch (Throwable e) {
      return false;
    } finally {
      this.isContinue = true;
    }
  }
}
