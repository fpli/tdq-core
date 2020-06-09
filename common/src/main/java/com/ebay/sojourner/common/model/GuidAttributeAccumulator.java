package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class GuidAttributeAccumulator {

  private GuidAttribute guidAttribute;
  private Map<Integer, Integer> signatureStates = new HashMap<>();

  public GuidAttributeAccumulator() {
    this.guidAttribute = new GuidAttribute();
    signatureStates.put(15, 0);
  }
}
