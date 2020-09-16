package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.EventHash;

public class HashCodeParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void init() throws Exception {
  }

  public void parse(RawEvent event, UbiEvent ubiEvent) {
    ubiEvent.setHashCode(EventHash.hashCode(ubiEvent));
  }
}
