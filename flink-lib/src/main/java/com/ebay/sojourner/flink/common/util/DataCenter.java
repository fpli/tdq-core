package com.ebay.sojourner.flink.common.util;

public enum DataCenter {
  RNO, SLC, LVS;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
