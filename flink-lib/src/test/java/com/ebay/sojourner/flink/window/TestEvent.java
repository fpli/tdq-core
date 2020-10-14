package com.ebay.sojourner.flink.window;

import java.io.Serializable;

public class TestEvent implements Serializable {

  private long key;
  private long timestamp;
  private long value;

  public TestEvent() {
  }

  public TestEvent(long key, long timestamp, long value) {
    this.key = key;
    this.timestamp = timestamp;
    this.value = value;
  }

  public long getKey() {
    return key;
  }

  public void setKey(long key) {
    this.key = key;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }
}
