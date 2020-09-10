package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

  @Test
  @DisplayName("sqr is undefined")
  public void test1() throws Exception {
    ubiEvent.setSqr("undefined");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .htm")
  public void test2() throws Exception {
    ubiEvent.setSqr("ebay.htm");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .asp")
  public void test3() throws Exception {
    ubiEvent.setSqr("ebay.asp");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .jsp")
  public void test4() throws Exception {
    ubiEvent.setSqr("ebay.jsp");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .gif")
  public void test5() throws Exception {
    ubiEvent.setSqr("ebay.gif");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .png")
  public void test6() throws Exception {
    ubiEvent.setSqr("ebay.png");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .pdf")
  public void test7() throws Exception {
    ubiEvent.setSqr("ebay.pdf");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .html")
  public void test8() throws Exception {
    ubiEvent.setSqr("ebay.html");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .php")
  public void test9() throws Exception {
    ubiEvent.setSqr("ebay.php");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .cgi")
  public void test10() throws Exception {
    ubiEvent.setSqr("ebay.cgi");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .jpeg")
  public void test11() throws Exception {
    ubiEvent.setSqr("ebay.jpeg");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .swf")
  public void test12() throws Exception {
    ubiEvent.setSqr("ebay.swf");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .txt")
  public void test13() throws Exception {
    ubiEvent.setSqr("ebay.txt");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .wav")
  public void test14() throws Exception {
    ubiEvent.setSqr("ebay.wav");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .zip")
  public void test15() throws Exception {
    ubiEvent.setSqr("ebay.zip");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .flv")
  public void test16() throws Exception {
    ubiEvent.setSqr("ebay.flv");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .dll")
  public void test17() throws Exception {
    ubiEvent.setSqr("ebay.dll");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .ico")
  public void test18() throws Exception {
    ubiEvent.setSqr("ebay.ico");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is end with .jpg")
  public void test19() throws Exception {
    ubiEvent.setSqr("ebay.jpg");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("sqr is contains hideoutput")
  public void test20() throws Exception {
    ubiEvent.setSqr("ebay.hideoutput");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is undefined")
  public void test21() throws Exception {
    ubiEvent.setUrlQueryString("undefined");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .gif")
  public void test22() throws Exception {
    ubiEvent.setUrlQueryString("ebay.gif");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .png")
  public void test23() throws Exception {
    ubiEvent.setUrlQueryString("ebay.png");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .pdf")
  public void test24() throws Exception {
    ubiEvent.setUrlQueryString("ebay.pdf");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .jpeg")
  public void test25() throws Exception {
    ubiEvent.setUrlQueryString("ebay.jpeg");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .swf")
  public void test26() throws Exception {
    ubiEvent.setUrlQueryString("ebay.swf");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .txt")
  public void test27() throws Exception {
    ubiEvent.setUrlQueryString("ebay.txt");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .wav")
  public void test28() throws Exception {
    ubiEvent.setUrlQueryString("ebay.wav");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .zip")
  public void test29() throws Exception {
    ubiEvent.setUrlQueryString("ebay.zip");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .flv")
  public void test30() throws Exception {
    ubiEvent.setUrlQueryString("ebay.flv");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .ico")
  public void test31() throws Exception {
    ubiEvent.setUrlQueryString("ebay.ico");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }

  @Test
  @DisplayName("urlQueryString is end with .jpg")
  public void test32() throws Exception {
    ubiEvent.setUrlQueryString("ebay.jpg");
    partialValidPageParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(false, ubiEvent.isPartialValidPage());
  }
}
