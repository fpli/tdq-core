package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParsersTestExpect {

  @JsonProperty("UbiEvent")
  private UbiEvent ubiEvent;
}
