package com.ebay.sojourner.business.ubd.rule;

import lombok.Data;

@Data
public class RulesTestCase {

  private String name;
  private String type;
  private RulesTestInputObjects input;
  private int expectResult;
}
