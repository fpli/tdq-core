package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.business.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.util.ParserConstants;
import com.ebay.sojourner.business.util.VaildateResult;
import com.ebay.sojourner.business.util.YamlUtil;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class CobrandParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static CobrandParser cobrandParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.COBRAND;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testCobrandParser1() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    caseItem = ParserConstants.CASE1;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser2() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setAppId(1281);
    caseItem = ParserConstants.CASE2;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser3() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setAppId(1232);
    caseItem = ParserConstants.CASE3;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser4() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setAppId(2736);
    caseItem = ParserConstants.CASE4;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser5() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1605657);
    ubiEvent.setAgentInfo("");
    caseItem = ParserConstants.CASE5;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser6() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1605657);
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE6;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser7() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1605657);
    ubiEvent.setAgentInfo("iPhone");
    caseItem = ParserConstants.CASE7;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser8() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1605657);
    ubiEvent.setAgentInfo("Windows NT 6.2");
    caseItem = ParserConstants.CASE8;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser9() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1605657);
    ubiEvent.setAgentInfo("Mozilla/5.0 ");
    caseItem = ParserConstants.CASE9;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser10() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(5938);
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE10;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser11() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(5938);
    ubiEvent.setAgentInfo("Mozilla/5.0 ");
    caseItem = ParserConstants.CASE11;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser12() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1468662);
    ubiEvent.setAgentInfo("Mozilla/5.0 ");
    caseItem = ParserConstants.CASE12;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser13() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1468662);
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE13;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser14() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1499158);
    caseItem = ParserConstants.CASE14;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser15() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1695768);
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE15;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser16() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(1695768);
    ubiEvent.setAgentInfo("Mozilla/5.0 ");
    caseItem = ParserConstants.CASE16;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser17() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=505");
    caseItem = ParserConstants.CASE17;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser18() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=506");
    caseItem = ParserConstants.CASE18;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser19() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=502");
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE19;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser20() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=502");
    ubiEvent.setAgentInfo("Mozilla/5.0 ");
    caseItem = ParserConstants.CASE20;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser21() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=507");
    caseItem = ParserConstants.CASE21;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }

  @Test
  public void testCobrandParser22() {
    cobrandParser = new CobrandParser();
    ubiEvent = new UbiEvent();
    ubiEvent.setPageId(123);
    ubiEvent.setApplicationPayload("pn=ebay");
    ubiEvent.setAgentInfo("HTC");
    caseItem = ParserConstants.CASE22;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        cobrandParser.init();
        cobrandParser.parse(entry.getKey(), ubiEvent);
        System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getCobrand()));
      }
    } catch (Exception e) {
      log.error("cobrand test fail!!!");
    }
  }
}
