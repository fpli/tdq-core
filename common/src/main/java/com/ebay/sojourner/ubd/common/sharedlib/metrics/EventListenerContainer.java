package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.ArrayList;
import java.util.List;

public class EventListenerContainer {

  private static List<EventListener> eventListenerList = new ArrayList<>();

  public static void init() {
    addListener(new AgentIPMetrics());
    addListener(new SessionStartDtMetrics());
    addListener(new AppIdMetrics());
    addListener(new SiteIdMetrics());
    addListener(new UserIdMetrics());
    addListener(new ReferrerMetrics());
    addListener(new CguidMetrics());
    addListener(new AgentStringMetrics());
    addListener(new PageIdMetrics());
    addListener(new FirstMappedUserIdMetrics());

  }

  private static void addListener(EventListener eventListener) {
    eventListenerList.add(eventListener);
  }

  public static void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    for (EventListener eventListener : eventListenerList) {
      eventListener.onEarlyEventChange(ubiEvent, ubiSession);
    }
  }

  public static void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    for (EventListener eventListener : eventListenerList) {
      eventListener.onLateEventChange(ubiEvent, ubiSession);
    }
  }

}
