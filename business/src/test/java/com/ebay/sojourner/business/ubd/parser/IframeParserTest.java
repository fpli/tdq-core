package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IframeParserTest {

  private static final Logger logger = Logger.getLogger(IframeParserTest.class);

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static IFrameParser iFrameParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.IFRAME;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testIframeParser1() {
    iFrameParser = new IFrameParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        iFrameParser.parse(entry.getKey(), ubiEvent);
        System.out.println(
            VaildateResult.validateString(entry.getValue(), String.valueOf(ubiEvent.isIframe())));
      }
    } catch (Exception e) {
      logger.error("iframe test fail!!!");
    }
  }

  @Test
  public void testIframeParser2() {
    iFrameParser = new IFrameParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        iFrameParser.parse(entry.getKey(), ubiEvent);
        System.out.println(
            VaildateResult.validateString(entry.getValue(), String.valueOf(ubiEvent.isIframe())));
      }
    } catch (Exception e) {
      logger.error("iframe test fail!!!");
    }
  }
}
