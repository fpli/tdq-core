package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.business.util.LoadRawEventAndExpect;
import com.ebay.sojourner.business.util.ParserConstants;
import com.ebay.sojourner.business.util.VaildateResult;
import com.ebay.sojourner.business.util.YamlUtil;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserIdParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static UserIdParser userIdParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.USERID;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testUserIdParser1() {
    userIdParser = new UserIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      userIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(VaildateResult.validateString(entry.getValue(), ubiEvent.getUserId()));
    }
  }

  @Test
  public void testUserIdParser2() {
    userIdParser = new UserIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      userIdParser.parse(entry.getKey(), ubiEvent);

      if (StringUtils.isBlank(ubiEvent.getUserId())) {
        System.out.println("true");
      }
    }
  }

  @Test
  public void testUserIdParser3() {
    userIdParser = new UserIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE3;

    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      userIdParser.parse(entry.getKey(), ubiEvent);

      if (StringUtils.isBlank(ubiEvent.getUserId())) {
        System.out.println("true");
      }
    }
  }
}
