package com.ebay.sojourner.flink.common.window;

import java.io.Serializable;

public class TestSession implements Serializable {

  private long start = -1;
  private long count = 0;
  private long sum = 0;

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public long getSum() {
    return sum;
  }

  public void setSum(long sum) {
    this.sum = sum;
  }

  public String toString() {
    return "(start=" + this.start + ", count=" + this.count + ", sum=" + this.sum + ")";
  }
}
