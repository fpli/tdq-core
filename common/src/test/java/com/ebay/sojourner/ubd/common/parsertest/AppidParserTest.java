package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.AppIdParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.ebay.sojourner.ubd.common.sharelib.Constants.*;

public class AppidParserTest {
    private static final Logger logger = Logger.getLogger(AppidParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static AppIdParser appIdParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = APPID;
        map = YamlUtil.getInstance().loadFileMap(FILEPATH);
    }

    @Test
    public void testAppidParser1() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }

    }

    @Test
    public void testAppidParser2() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser3() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser4() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser5() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE5;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }


    @Test
    public void testAppidParser6() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE6;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser7() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE7;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser8() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE8;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser9() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE9;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser10() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE10;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser11() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE11;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser12() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE12;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
//            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser13() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE13;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
//            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser14() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE14;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
//            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser15() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE15;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
//            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }

    @Test
    public void testAppidParser16() {
        appIdParser = new AppIdParser();
        ubiEvent = new UbiEvent();
        caseItem = CASE16;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            appIdParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry. getValue(), TypeTransUtil.IntegerToString(ubiEvent.getAppId())));
//            System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getAppId()));
        }
    }
}
