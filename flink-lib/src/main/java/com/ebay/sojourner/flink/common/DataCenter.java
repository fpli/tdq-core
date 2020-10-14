package com.ebay.sojourner.flink.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataCenter {
  RNO("RNO"),
  SLC("SLC"),
  LVS("LVS");

  private final String value;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
