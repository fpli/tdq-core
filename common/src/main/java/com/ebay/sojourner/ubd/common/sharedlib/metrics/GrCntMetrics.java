package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class GrCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, LkpListener {

  private ArrayList<String> viPGT;
  private Map<Integer, String[]> pageFmlyNameMap;
  private volatile LkpManager lkpManager;
  private boolean isContinue;

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setGrCnt(0);
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
        && pageId != -1
        && ((pageFmlyNameMap.containsKey(pageId) && "GR".equals(pageFmlyNameMap.get(pageId)[1]))
        || (getImPGT(event) != null && "GR".equals(getImPGT(event))))) {
      sessionAccumulator
          .getUbiSession()
          .setGrCnt(sessionAccumulator.getUbiSession().getGrCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {
    lkpManager = new LkpManager(this, LkpEnum.pageFmly);
    isContinue = true;
    pageFmlyNameMap = lkpManager.getPageFmlyMaps();
    viPGT =
        new ArrayList<>(
            PropertyUtils.parseProperty(
                UBIConfig.getString(Property.VI_EVENT_VALUES), Property.PROPERTY_DELIMITER));
  }

  private String getImPGT(UbiEvent event) {
    if (event.getPageId() == 1521826
        && StringUtils.isNotBlank(SOJNVL.getTagValue(event.getApplicationPayload(), "pgt"))
        && viPGT.contains(SOJNVL.getTagValue(event.getApplicationPayload(), "pgt"))) {
      return "VI";
    }
    if (event.getPageId() == 2066804
        && StringUtils.isNotBlank(event.getUrlQueryString())
        && (event.getUrlQueryString().startsWith("/itm/like")
        || event.getUrlQueryString().startsWith("/itm/future"))) {
      return "VI";
    }
    if (event.getPageId() == 1521826 || event.getPageId() == 2066804) {
      return "GR";
    }
    return null;
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
