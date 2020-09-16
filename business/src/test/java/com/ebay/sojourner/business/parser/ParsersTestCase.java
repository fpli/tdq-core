package com.ebay.sojourner.business.parser;

import lombok.Data;

@Data
public class ParsersTestCase {

  private String name;
  private ParsersTestInputObjects inputs;
  private ParsersTestExpect expect;
}
