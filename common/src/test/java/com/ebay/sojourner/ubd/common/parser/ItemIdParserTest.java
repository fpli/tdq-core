package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.ItemIdParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ItemIdParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static ItemIdParser itemIdParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.ITEM;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testItemIdParser1() {
        itemIdParser = new ItemIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            itemIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getItemId())));
        }
    }

    @Test
    public void testItemIdParser2() {
        itemIdParser = new ItemIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            itemIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(),TypeTransUtil.LongToString(ubiEvent.getItemId())));
        }
    }
}
