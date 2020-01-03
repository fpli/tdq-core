package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIdParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PageIdParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static PageIdParser pageIdParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.PAGEID;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testPageIdParser1() {
        pageIdParser = new PageIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            pageIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(),ubiEvent.getPageId()));
        }
    }

    @Test
    public void testPageIdParser2() {
        pageIdParser = new PageIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            pageIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(),ubiEvent.getPageId()));
        }
    }

    @Test
    public void testPageIdParser3() {
        pageIdParser = new PageIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            pageIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(),ubiEvent.getPageId()));
        }
    }

    @Test
    public void testPageIdParser4() {
        pageIdParser = new PageIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            pageIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(),ubiEvent.getPageId()));
        }
    }

    @Test
    public void testPageIdParser5() {
        pageIdParser = new PageIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE5;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            pageIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(),ubiEvent.getPageId()));
        }
    }
}
