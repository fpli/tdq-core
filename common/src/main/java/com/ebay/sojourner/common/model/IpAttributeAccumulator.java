package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class IpAttributeAccumulator {

  private IpAttribute ipAttribute;
  private Map<Integer, SignatureInfo> signatureStatus;

  public IpAttributeAccumulator() {
    this.ipAttribute = new IpAttribute();
    signatureStatus = new HashMap<>();
  }
}
