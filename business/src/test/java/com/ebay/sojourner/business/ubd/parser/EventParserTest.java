package com.ebay.sojourner.business.ubd.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventParserTest {

  EventParser eventParser;

  @Test
  void test_eventParser() throws Exception {
    eventParser = new EventParser();
    Assertions.assertThat(eventParser.fieldParsers.size()).isEqualTo(27);
  }
}
