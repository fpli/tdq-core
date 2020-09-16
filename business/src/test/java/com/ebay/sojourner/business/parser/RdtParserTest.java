package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RdtParserTest {

  private UbiEvent ubiEvent;
  private RdtParser rdtParser;
  private RawEvent rawEvent;

  @BeforeEach
  public void setup() {
    rdtParser = new RdtParser();
    ubiEvent = new UbiEvent();
    rawEvent = new RawEvent();
  }

  @Test
  @DisplayName("applicationPayload is null")
  public void test1() {
    rdtParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isRdt());
  }

  @Test
  @DisplayName("applicationPayload is not null and rdt equals 0")
  public void test2() {
    ubiEvent.setApplicationPayload("rdt=0");
    rdtParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isRdt());
  }

  @Test
  @DisplayName("applicationPayload is not null and rdt not equals 0")
  public void test3() {
    ubiEvent.setApplicationPayload("rdt=1");
    rdtParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(true, ubiEvent.isRdt());
  }
}