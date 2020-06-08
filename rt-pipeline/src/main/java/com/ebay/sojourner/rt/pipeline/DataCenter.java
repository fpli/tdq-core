package com.ebay.sojourner.rt.pipeline;

public enum DataCenter {
  RNO, SLC, LVS;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
