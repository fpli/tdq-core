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

public class ItemIdParserTest {

  private static UbiEvent ubiEvent = null;
  private static String parser = null;
  private static String caseItem = null;
  private static ItemIdParser itemIdParser = null;
  private static HashMap<String, Object> map = null;

  @BeforeAll
  public static void initParser() {
    parser = ParserConstants.ITEM;
    map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
  }

  @Test
  public void testItemIdParser1() {
    itemIdParser = new ItemIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE1;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      itemIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getItemId())));
    }
  }

  @Test
  public void testItemIdParser2() {
    itemIdParser = new ItemIdParser();
    ubiEvent = new UbiEvent();
    caseItem = ParserConstants.CASE2;
    HashMap<RawEvent, Object> rawEventAndExpectResult =
        LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
    for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
      itemIdParser.parse(entry.getKey(), ubiEvent);
      System.out.println(
          VaildateResult.validateString(
              entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getItemId())));
    }
  }
}
