package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParsersTestInputObjects {

  @JsonProperty("RawEvent")
  private RawEvent rawEvent;
}
