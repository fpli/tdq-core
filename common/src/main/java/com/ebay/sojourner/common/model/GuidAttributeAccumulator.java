package com.ebay.sojourner.common.model;

import java.util.HashMap;
import lombok.Data;

@Data
public class GuidAttributeAccumulator {

  private GuidAttribute guidAttribute;
  private HashMap<Integer, Integer> botFlagStatus = new HashMap<>();

  public GuidAttributeAccumulator() {
    this.guidAttribute = new GuidAttribute();
    botFlagStatus.put(15, 0);
  }
}
