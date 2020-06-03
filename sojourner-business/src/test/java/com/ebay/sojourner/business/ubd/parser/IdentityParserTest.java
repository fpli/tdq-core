package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.business.ubd.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.ubd.util.ParserConstants;
import com.ebay.sojourner.business.ubd.util.VaildateResult;
import com.ebay.sojourner.business.ubd.util.YamlUtil;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IdentityParserTest {

  private static final Logger logger = Logger.getLogger(IdentityParserTest.class);

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static IdentityParser identityParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.IDENTITY;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testIdentityParser() {
    identityParser = new IdentityParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    try {
      HashMap<RawEvent, Object> rawEventAndExpectResult =
          LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
      for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
        identityParser.parse(entry.getKey(), ubiEvent);
        System.out.println(
            VaildateResult.validateString(entry.getValue(), ubiEvent.getApplicationPayload()));
        System.out.println(ubiEvent.getGuid());
      }
    } catch (Exception e) {
      logger.error("identity test fail!!!");
    }
  }
}
