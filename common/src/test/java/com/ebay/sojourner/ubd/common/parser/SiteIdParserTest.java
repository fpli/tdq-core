package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.SiteIdParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SiteIdParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static SiteIdParser siteIdParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.SITE;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testSiteIdParser1() {
        siteIdParser = new SiteIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siteIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getSiteId()));
        }
    }

    @Test
    public void testSiteIdParser2() {
        siteIdParser = new SiteIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siteIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getSiteId()));
        }
    }

    @Test
    public void testSiteIdParser3() {
        siteIdParser = new SiteIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siteIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getSiteId()));
        }
    }

    @Test
    public void testSiteIdParser4() {
        siteIdParser = new SiteIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siteIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getSiteId()));
        }
    }

    @Test
    public void testSiteIdParser5() {
        siteIdParser = new SiteIdParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE5;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siteIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getSiteId()));
        }
    }
}
