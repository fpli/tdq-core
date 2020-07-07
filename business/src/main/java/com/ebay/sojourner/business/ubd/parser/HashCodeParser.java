package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.EventHash;
import org.apache.log4j.Logger;

public class HashCodeParser implements FieldParser<RawEvent, UbiEvent> {

  private static final Logger log = Logger.getLogger(HashCodeParser.class);
  private static final String CIID_TAG = "ciid";

  @Override
  public void init() throws Exception {
  }

  public void parse(RawEvent event, UbiEvent ubiEvent) {
    ubiEvent.setHashCode(EventHash.hashCode(ubiEvent));
  }
}
