package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReguParserTest {

  private UbiEvent ubiEvent;
  private ReguParser reguParser;
  private RawEvent rawEvent;

  @BeforeEach
  public void setup() {
    reguParser = new ReguParser();
    ubiEvent = new UbiEvent();
    rawEvent = new RawEvent();
  }

  @Test
  @DisplayName("applicationPayload is not null and regu is null")
  public void test1() {
    reguParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(0, ubiEvent.getRegu());
  }

  @Test
  @DisplayName("applicationPayload is not null and regu is not null")
  public void test2() {
    ubiEvent.setApplicationPayload("regU=1");
    reguParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(1, ubiEvent.getRegu());
  }
}
