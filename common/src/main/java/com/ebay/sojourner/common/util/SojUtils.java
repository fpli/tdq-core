package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.UbiEvent;

public class SojUtils {

  public static boolean isRover3084Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return 3084 == event.getPageId();
  }

  public static boolean isRover3085Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return event.getPageId() == 3085;
  }

  public static boolean isRover3962Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return event.getPageId() == 3962;
  }

  public static boolean isRoverClick(UbiEvent event) {
    return IntermediateLkp.getInstance().getRoverPageSet().contains(event.getPageId());
  }

  public static boolean isScEvent(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    return !event.isRdt()
        && !event.isIframe()
        // || urlQueryString.matches("(/roverimp|.*SojPageView).*")
        && !IntermediateLkp.getInstance().getScPageSet1().contains(pageId)
        && !IntermediateLkp.getInstance().getScPageSet2().contains(pageId);
  }
}
