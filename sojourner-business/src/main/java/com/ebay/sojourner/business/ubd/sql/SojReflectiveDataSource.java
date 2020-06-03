package com.ebay.sojourner.business.ubd.sql;

public class SojReflectiveDataSource {

  public SojReflectiveEvent[] idl_event = {new SojReflectiveEvent.Builder().build()};

  public SojReflectiveDataSource() {
  }

  public void updateData(SojReflectiveEvent newEvent) {
    idl_event[0] = newEvent;
  }

  public void update(SojReflectiveEvent... events) {
    idl_event = new SojReflectiveEvent[events.length];
  }

  public String toString() {
    return "SojSchema";
  }
}
