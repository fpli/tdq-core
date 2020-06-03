package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.TypeTransUtil;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppidParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static AppIdParser appIdParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.APPID;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testAppidParser1() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser2() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser3() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser4() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE4;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser5() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE5;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser6() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE6;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser7() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE7;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser8() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE8;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser9() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE9;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser10() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE10;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getAppId()));
    }
  }

  @Test
  public void testAppidParser11() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE11;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getAppId()));
    }
  }

  @Test
  public void testAppidParser12() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE12;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser13() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE13;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser14() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE14;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser15() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE15;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }

  @Test
  public void testAppidParser16() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE16;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      appIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
    }
  }
}
