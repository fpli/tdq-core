package com.ebay.sojourner.ubd.common.sharedlib.parser;

import static com.ebay.sojourner.ubd.common.util.ParserConstants.APPID;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE1;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE10;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE11;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE12;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE13;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE14;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE15;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE16;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE2;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE3;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE4;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE5;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE6;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE7;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE8;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.CASE9;
import static com.ebay.sojourner.ubd.common.util.ParserConstants.FILEPATH;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import com.ebay.sojourner.ubd.common.util.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
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
    parser = APPID;
    map = YamlUtil.getInstance().loadFileMap(FILEPATH);
  }

  @Test
  public void testAppidParser1() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    caseItem = CASE1;

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
    caseItem = CASE2;

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
    caseItem = CASE3;

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
    caseItem = CASE4;

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
    caseItem = CASE5;

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
    caseItem = CASE6;

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
    caseItem = CASE7;

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
    caseItem = CASE8;

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
    caseItem = CASE9;

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
    caseItem = CASE10;

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
    caseItem = CASE11;

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
    caseItem = CASE12;

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
    caseItem = CASE13;

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
    caseItem = CASE14;

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
    caseItem = CASE15;

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
    caseItem = CASE16;

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
