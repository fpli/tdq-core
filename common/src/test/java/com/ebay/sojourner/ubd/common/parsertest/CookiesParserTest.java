package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.CookiesParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CookiesParserTest {
    private static final Logger logger = Logger.getLogger(CookiesParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static CookiesParser cookiesParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.COOKIES;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testCookiesParser1() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser2() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser3() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser4() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }


}
