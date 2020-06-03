package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

public interface EventListener {

  void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession);

  void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession);

}
