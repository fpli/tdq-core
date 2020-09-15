package com.ebay.sojourner.common.model.attribute;

import lombok.Data;

@Data
public class AttributesTestCase {

  private String name;
  private String type;
  private AttributeTestInputObjects input;
  private String expectResult;
}
