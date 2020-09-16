package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParsersTestExpect {

  @JsonProperty("UbiEvent")
  private UbiEvent ubiEvent;
}
