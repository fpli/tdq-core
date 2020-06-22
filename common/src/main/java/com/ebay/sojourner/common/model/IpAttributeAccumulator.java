package com.ebay.sojourner.common.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class IpAttributeAccumulator {

  private IpAttribute ipAttribute;
  private Map<Integer, SignatureInfo> signatureStatus = new HashMap<>();

  public IpAttributeAccumulator() {
    this.ipAttribute = new IpAttribute();
    signatureStatus = new HashMap<>();
    //    signatureStatus.put(7, 0);
    //    signatureStatus.put(222, 0);
    //    signatureStatus.put(223, 0);
  }
}
