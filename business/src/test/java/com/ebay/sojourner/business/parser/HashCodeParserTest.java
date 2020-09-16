package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashCodeParserTest{

  private HashCodeParser hashCodeParser;
  private UbiEvent ubiEvent;
  private RawEvent rawEvent;

  @BeforeEach
  public void setup() {
    hashCodeParser = new HashCodeParser();
    ubiEvent = new UbiEvent();
    rawEvent = new RawEvent();
  }

  @Test
  public void testHashCode() {
    hashCodeParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(-37, ubiEvent.getHashCode());
  }
}
