package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import java.util.Map;

public class FindingFlagParser implements FieldParser<RawEvent, UbiEvent>, LkpListener {

  private volatile LkpManager lkpManager;
  private Map<Integer, Integer> findingFlagMap;
  private boolean isContinue;
  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    while(!isContinue){
      Thread.sleep(10);
    }
    Integer pageId = ubiEvent.getPageId();
    if (findingFlagMap.containsKey(pageId)) {
      ubiEvent.setBitVal(findingFlagMap.get(pageId));
    }
  }

  @Override
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.findingFlag);
    findingFlagMap = lkpManager.getFindingFlagMap();
    isContinue=true;
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {
    try {
      this.isContinue=false;
      findingFlagMap = lkpManager.getFindingFlagMap();
      return true;
    } catch (Throwable e) {
      return false;
    }
    finally {
      this.isContinue=true;
    }
  }
}
