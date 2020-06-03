package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.TypeTransUtil;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SiidParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static SiidParser siidParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.SIID;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testSiidParser1() {
    siidParser = new SiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      siidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
    }
  }

  @Test
  public void testSiidParser2() {
    siidParser = new SiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      siidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
    }
  }

  @Test
  public void testSiidParser3() {
    siidParser = new SiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      siidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
    }
  }

  @Test
  public void testSiidParser4() {
    siidParser = new SiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE4;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      siidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
    }
  }
}
