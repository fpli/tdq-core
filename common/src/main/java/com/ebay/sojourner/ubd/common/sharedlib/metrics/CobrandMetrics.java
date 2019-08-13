package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

public class CobrandMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
  private Set<Integer> invalidPageIds;
  private Integer firstCobrand;
  private static UBIConfig ubiConfig;
  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    firstCobrand = null;
    sessionAccumulator.getUbiSession().setFirstCorbrand(null);
    sessionAccumulator.getUbiSession().setCobrand(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    if (sessionAccumulator.getUbiSession().getCobrand() == null && event.getIframe() == 0 && event.getRdt() == 0
        && !invalidPageIds.contains(event.getPageId())) {
      sessionAccumulator.getUbiSession().setCobrand(event.getCobrand());
    }
    if (sessionAccumulator.getUbiSession().getFirstCorbrand() == null) {
      sessionAccumulator.getUbiSession().setFirstCorbrand(event.getCobrand());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
    if (sessionAccumulator.getUbiSession().getCobrand() == null) {
      sessionAccumulator.getUbiSession().setCobrand(sessionAccumulator.getUbiSession().getFirstCorbrand());
    }
  }

  @Override
  public void init() throws Exception {
    InputStream resourceAsStream = CobrandMetrics.class.getResourceAsStream("/ubi.properties");
    ubiConfig = UBIConfig.getInstance(resourceAsStream);
    invalidPageIds = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.INVALID_PAGE_IDS), Property.PROPERTY_DELIMITER);
  }
}
