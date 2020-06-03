package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RdtParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static RdtParser rdtParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.RDT;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testRdtParser1() {
    rdtParser = new RdtParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      rdtParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(entry.getValue(), String.valueOf(ubiEvent.isRdt())));
    }
  }

  @Test
  public void testRdtParser2() {
    rdtParser = new RdtParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      rdtParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(entry.getValue(), String.valueOf(ubiEvent.isRdt())));
    }
  }

  @Test
  public void testRdtParser3() {
    rdtParser = new RdtParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      rdtParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(entry.getValue(), String.valueOf(ubiEvent.isRdt())));
    }
  }
}
