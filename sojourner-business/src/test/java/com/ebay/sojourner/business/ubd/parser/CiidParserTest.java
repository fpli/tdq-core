package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.TypeTransUtil;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CiidParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static CiidParser ciidParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.CIID;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testCiidParser1() {
    ciidParser = new CiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      ciidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getCurrentImprId())));
    }
  }

  @Test
  public void testCiidParser2() {
    ciidParser = new CiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      ciidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getCurrentImprId())));
    }
  }

  @Test
  public void testCiidParser3() {
    ciidParser = new CiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      ciidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getCurrentImprId())));
    }
  }

  @Test
  public void testCiidParser4() {
    ciidParser = new CiidParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE4;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      ciidParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getCurrentImprId())));
    }
  }
}
