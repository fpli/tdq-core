package com.ebay.sojourner.ubd.common.sql;

public class SojReflectiveDataSource {

  public SojReflectiveEvent[] ubiEvents = {new SojReflectiveEvent.Builder().build()};

  public SojReflectiveDataSource() {
  }

  public void updateData(SojReflectiveEvent newEvent) {
    ubiEvents[0] = newEvent;
  }

  public void update(SojReflectiveEvent... events) {
    ubiEvents = new SojReflectiveEvent[events.length];
  }

  public String toString() {
    return "SojSchema";
  }
}
