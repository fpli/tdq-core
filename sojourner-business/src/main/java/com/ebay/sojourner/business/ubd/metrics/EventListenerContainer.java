package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventListenerContainer {

  private static volatile EventListenerContainer eventListenerContainer;

  private CopyOnWriteArrayList<EventListener> eventListenerList = new CopyOnWriteArrayList<>();

  private EventListenerContainer() {
    this.init();
    try {
      this.initListener();
    } catch (Exception e) {
      log.error("eventListener init error", e.getMessage());
    }

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
    addListener(new OsMetrics());
    addListener(new BrowserMetrics());
    addListener(new DeviceMetrics());
    addListener(new AddressMetrics());
  }

  private void initListener() throws Exception {
    for (EventListener eventListener : eventListenerList) {
      if (eventListener instanceof AgentIPMetrics) {
        ((AgentIPMetrics) eventListener).init();
      }
      if (eventListener instanceof SessionStartDtMetrics) {
        ((SessionStartDtMetrics) eventListener).init();
      }
      if (eventListener instanceof AppIdMetrics) {
        ((AppIdMetrics) eventListener).init();
      }
      if (eventListener instanceof SiteIdMetrics) {
        ((SiteIdMetrics) eventListener).init();
      }
      if (eventListener instanceof UserIdMetrics) {
        ((UserIdMetrics) eventListener).init();
      }
      if (eventListener instanceof ReferrerMetrics) {
        ((ReferrerMetrics) eventListener).init();
      }
      if (eventListener instanceof CguidMetrics) {
        ((CguidMetrics) eventListener).init();
      }
      if (eventListener instanceof AgentStringMetrics) {
        ((AgentStringMetrics) eventListener).init();
      }
      if (eventListener instanceof PageIdMetrics) {
        ((PageIdMetrics) eventListener).init();
      }
      if (eventListener instanceof FirstMappedUserIdMetrics) {
        ((FirstMappedUserIdMetrics) eventListener).init();
      }
      if (eventListener instanceof OsMetrics) {
        ((OsMetrics) eventListener).init();
      }
      if (eventListener instanceof BrowserMetrics) {
        ((BrowserMetrics) eventListener).init();
      }
      if (eventListener instanceof DeviceMetrics) {
        ((DeviceMetrics) eventListener).init();
      }
      if (eventListener instanceof AddressMetrics) {
        ((AddressMetrics) eventListener).init();
      }
    }
  }
}
