package com.ebay.sojourner.common.model;

import java.util.HashMap;
import lombok.Data;

@Data
public class IpAttributeAccumulator {

  private IpAttribute ipAttribute;
  private HashMap<Integer, Integer> botFlagStatus = new HashMap<>();

  public IpAttributeAccumulator() {
    this.ipAttribute = new IpAttribute();
    botFlagStatus.put(7, 0);
    botFlagStatus.put(222, 0);
    botFlagStatus.put(223, 0);
  }
}
