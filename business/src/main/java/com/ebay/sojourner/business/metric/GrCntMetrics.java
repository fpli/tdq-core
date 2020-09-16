package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.util.SOJNVL;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.UBIConfig;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class GrCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private ArrayList<String> viPGT;

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setGrCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    Map<Integer, String[]> pageFmlyNameMap = LkpManager.getInstance().getPageFmlyMaps();
    Integer pageId = event.getPageId();
    if (!event.isRdt()
        && !event.isIframe()
        && event.isPartialValidPage()
        && pageId != -1
        && ((pageFmlyNameMap.containsKey(pageId) && "GR"
        .equals(pageFmlyNameMap.get(pageId)[1]))
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
}
