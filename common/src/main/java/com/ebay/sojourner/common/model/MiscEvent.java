package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiscEvent {

  private long eventCreateTimestamp;
  private String sojA;
  private String sojC;
  private String sojK;
  private String clientData;

}
