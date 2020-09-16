package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PageIdParserSpecialTest {

  private PageIdParser pageIdParser;
  private UbiEvent ubiEvent;
  private RawEvent rawEvent;

  @BeforeEach
  public void setup() {
    pageIdParser = new PageIdParser();
    ubiEvent = new UbiEvent();
    rawEvent = new RawEvent();
  }

  @Test
  @DisplayName("ubiEvent guid is not null and pageid is not null")
  public void test1() {
    Map<String, String> sojA = new HashMap<>();
    sojA.put("a","a");
    Map<String, String> sojC = new HashMap<>();
    sojC.put("c", "b");
    Map<String, String> sojK = new HashMap<>();
    sojK.put("p", "123");
    rawEvent.setSojA(sojA);
    rawEvent.setSojC(sojC);
    rawEvent.setSojK(sojK);
    ubiEvent.setGuid("ebay");
    pageIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(123, ubiEvent.getPageId());
  }

  @Test
  @DisplayName("ubiEvent guid is not null and pageid is null")
  public void test2() {
    Map<String, String> sojA = new HashMap<>();
    sojA.put("a","a");
    Map<String, String> sojC = new HashMap<>();
    sojC.put("c", "b");
    Map<String, String> sojK = new HashMap<>();
    sojK.put("b", "123");
    rawEvent.setSojA(sojA);
    rawEvent.setSojC(sojC);
    rawEvent.setSojK(sojK);
    ubiEvent.setGuid("ebay");
    pageIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(-1, ubiEvent.getPageId());
  }

  @Test
  @DisplayName("ubiEvent guid is null and pageid is null")
  public void test3() {
    Map<String, String> sojA = new HashMap<>();
    sojA.put("a","a");
    Map<String, String> sojC = new HashMap<>();
    sojC.put("c", "b");
    Map<String, String> sojK = new HashMap<>();
    sojK.put("b", "123");
    rawEvent.setSojA(sojA);
    rawEvent.setSojC(sojC);
    rawEvent.setSojK(sojK);
    pageIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(-1, ubiEvent.getPageId());
  }

  @Test
  @DisplayName("ubiEvent guid is null and pageid is not null")
  public void test4() {
    Map<String, String> sojA = new HashMap<>();
    sojA.put("a","a");
    Map<String, String> sojC = new HashMap<>();
    sojC.put("c", "b");
    Map<String, String> sojK = new HashMap<>();
    sojK.put("p", "123");
    rawEvent.setSojA(sojA);
    rawEvent.setSojC(sojC);
    rawEvent.setSojK(sojK);
    pageIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(-1, ubiEvent.getPageId());
  }
}
