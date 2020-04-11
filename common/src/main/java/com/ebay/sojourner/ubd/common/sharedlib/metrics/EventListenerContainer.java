package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.ArrayList;
import java.util.List;

public class EventListenerContainer {

  private static volatile EventListenerContainer eventListenerContainer;
  private List<EventListener> eventListenerList = new ArrayList<>();

  private EventListenerContainer() {
    this.init();
  }

  public static EventListenerContainer getInstance() {
    if (eventListenerContainer == null) {
      synchronized (EventListenerContainer.class) {
        if (eventListenerContainer == null) {
          eventListenerContainer = new EventListenerContainer();
        }
      }
    }
    return eventListenerContainer;
  }

  private void addListener(EventListener eventListener) {
    eventListenerList.add(eventListener);
  }

  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    for (EventListener eventListener : eventListenerList) {
      eventListener.onEarlyEventChange(ubiEvent, ubiSession);
    }
  }

  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    for (EventListener eventListener : eventListenerList) {
      eventListener.onLateEventChange(ubiEvent, ubiSession);
    }
  }

  private void init() {
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

}
