package com.ebay.sojourner.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class IpSignature implements Signature, Serializable {

  private Map<Integer, Set<Integer>> ipBotSignature = new HashMap<>();

  public IpSignature() {
  }
}
