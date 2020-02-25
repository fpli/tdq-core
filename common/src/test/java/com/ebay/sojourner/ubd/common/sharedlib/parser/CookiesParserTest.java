package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.ParserConstants;
import com.ebay.sojourner.ubd.common.util.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.util.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

public class CookiesParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static CookiesParser cookiesParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = ParserConstants.COOKIES;
        map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
    }

    @Test
    public void testCookiesParser1() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser2() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser3() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }

    @Test
    public void testCookiesParser4() {
        cookiesParser = new CookiesParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            cookiesParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getCookies()));
        }
    }


}
