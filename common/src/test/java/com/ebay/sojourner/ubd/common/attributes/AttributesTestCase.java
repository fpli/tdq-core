package com.ebay.sojourner.ubd.common.attributes;

import lombok.Data;

@Data
public class AttributesTestCase {
  private String name;
  private String type;
  private AttributeTestInputObjects input;
  private String expectResult;
}
