package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.ClickIdParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClickIdParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static ClickIdParser clickIdParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.CLICKID;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testClickIdParser1() {
        clickIdParser = new ClickIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            clickIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
        }
    }

    @Test
    public void testClickIdParser2() {
        clickIdParser = new ClickIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            clickIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
        }
    }

    @Test
    public void testClickIdParser3() {
        clickIdParser = new ClickIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            clickIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
        }
    }

    @Test
    public void testClickIdParser4() {
        clickIdParser = new ClickIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            clickIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));
        }
    }

    @Test
    public void testClickIdParser5() {
        clickIdParser = new ClickIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE5;
        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            clickIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getClickId()));

        }
    }
}