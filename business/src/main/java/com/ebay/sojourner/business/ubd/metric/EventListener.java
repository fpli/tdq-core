package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;

public interface EventListener {

  void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession);

  void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession);

}
