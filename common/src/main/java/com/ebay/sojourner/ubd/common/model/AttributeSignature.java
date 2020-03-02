package com.ebay.sojourner.ubd.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class AttributeSignature {
  private Map<String, Set<Integer>> attributeSignature = new HashMap<>();

  public AttributeSignature() {}
}
