package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class IpAttributeAccumulator {

  private IpAttribute ipAttribute;
  private Map<Integer, Integer> signatureStates = new HashMap<>();

  public IpAttributeAccumulator() {
    this.ipAttribute = new IpAttribute();
    signatureStates.put(7, 0);
    signatureStates.put(222, 0);
    signatureStates.put(223, 0);
  }
}
