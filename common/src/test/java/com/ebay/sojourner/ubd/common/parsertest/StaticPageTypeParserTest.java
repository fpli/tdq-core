package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.StaticPageTypeParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StaticPageTypeParserTest {
    private static final Logger logger = Logger.getLogger(StaticPageTypeParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static StaticPageTypeParser staticPageTypeParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.STATICPAGE;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    //rdt and pageId not null,itemId not null,vtNewIdsMap not contains pageId
    @Test
    public void testStaticPageTypeParser1() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        ubiEvent.setRdt(456);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(789L);
        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //rdt and pageId not null,vtNewIdsMap contains pageId,itemId is null,lkup is default,flags not null
   @Test
    public void testStaticPageTypeParser2() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(4737);
        ubiEvent.setRdt(456);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE2;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //rdt and pageId is null,other not care
    @Test
    public void testStaticPageTypeParser3() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(null);
        ubiEvent.setRdt(null);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE3;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 284,itemId is null
    @Test
    public void testStaticPageTypeParser4() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(284);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE4;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 284,itemId not null
    @Test
    public void testStaticPageTypeParser5() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(284);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(1L);
        caseItem = Constants.CASE5;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 5414,itemId not null
    @Test
    public void testStaticPageTypeParser6() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(5414);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(1L);
        caseItem = Constants.CASE6;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 4018,itemId is not null
    @Test
    public void testStaticPageTypeParser7() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(4018);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(1L);
        caseItem = Constants.CASE7;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 4018,itemId is null
    @Test
    public void testStaticPageTypeParser8() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(4018);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE8;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 3994,itemId not null
    @Test
    public void testStaticPageTypeParser9() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(3994);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(2L);
        caseItem = Constants.CASE9;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //rdt and pageId not null,vtNewIdsMap contains pageId,pageId is 1637,itemId is null,lkup is default,flags is null
    @Test
    public void testStaticPageTypeParser10() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(4737);
        ubiEvent.setRdt(456);
        ubiEvent.setFlags("");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE10;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //rdt and pageId not null,vtNewIdsMap contains pageId,pageId is 4737,itemId is null,lkup is default,flags length is 46
    @Test
    public void testStaticPageTypeParser11() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(4737);
        ubiEvent.setRdt(456);
        ubiEvent.setFlags("1234567890123456789012345678901234567890abcdefg");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE11;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //rdt and pageId not null,vtNewIdsMap contains pageId,pageId is 1637,itemId is null,lkup is default,flags length is 46
    @Test
    public void testStaticPageTypeParser12() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(1637);
        ubiEvent.setRdt(456);
        ubiEvent.setFlags("1234567890123$56789012345678901234567890abcdzfg");
        ubiEvent.setItemId(null);
        caseItem = Constants.CASE12;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 3994,itemId not null
    @Test
    public void testStaticPageTypeParser13() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(2499);
        ubiEvent.setRdt(2019);
        ubiEvent.setFlags("ebay");
        ubiEvent.setItemId(2L);
        caseItem = Constants.CASE13;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }

    //pageId is 5414,itemId not null,rdt is 1
    @Test
    public void testStaticPageTypeParser14() {
        staticPageTypeParser = new StaticPageTypeParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(3897);
        ubiEvent.setRdt(1);
        ubiEvent.setFlags("haxiboeBay");
        ubiEvent.setItemId(1L);
        caseItem = Constants.CASE14;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                staticPageTypeParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getStaticPageType()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("staticpage test fail!!!");
        }
    }
}
