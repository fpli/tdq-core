package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PartialValidPageParserSpecialTest {

  private PartialValidPageParser partialValidPageParser;
  private RawEvent rawEvent;
  private UbiEvent ubiEvent;

  @BeforeEach
  public void setup() {
    partialValidPageParser = new PartialValidPageParser();
    rawEvent = new RawEvent();
    ubiEvent = new UbiEvent();
  }

  @ParameterizedTest
  @DisplayName("cases on sqr")
  @ValueSource(strings = {"undefined", "ebay.htm", "ebay.asp", "ebay.jsp", "ebay.gif", "ebay.png",
      "ebay.pdf", "ebay.html", "ebay.php", "ebay.cgi", "ebay.jpeg", "ebay.swf", "ebay.txt",
      "ebay.wav", "ebay.zip", "ebay.flv", "ebay.dll", "ebay.ico", "ebay.jpg", "ebay.hideoutput"})
  public void test_sqr(String input) throws Exception {
    ubiEvent.setSqr(input);
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @ParameterizedTest
  @DisplayName("cases on urlQueryString")
  @ValueSource(strings = {"undefined", "ebay.gif", "ebay.png", "ebay.pdf", "ebay.jpeg", "ebay.swf",
      "ebay.txt", "ebay.wav", "ebay.zip", "ebay.flv", "ebay.ico", "ebay.jpg"})
  public void test_urlQueryString(String input) throws Exception {
    ubiEvent.setUrlQueryString(input);
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }
}
