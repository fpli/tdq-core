package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClickIdParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static ClickIdParser clickIdParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.CLICKID;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testClickIdParser1() {
    clickIdParser = new ClickIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      clickIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
    }
  }

  @Test
  public void testClickIdParser2() {
    clickIdParser = new ClickIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      clickIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
    }
  }

  @Test
  public void testClickIdParser3() {
    clickIdParser = new ClickIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      clickIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
    }
  }

  @Test
  public void testClickIdParser4() {
    clickIdParser = new ClickIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE4;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      clickIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
    }
  }

  @Test
  public void testClickIdParser5() {
    clickIdParser = new ClickIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE5;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      clickIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
    }
  }
}
