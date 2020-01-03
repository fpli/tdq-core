package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PartialValidPageParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PartialValidPageParserTest {
    private static final Logger logger = Logger.getLogger(PartialValidPageParser.class);

    private static UbiEvent ubiEvent = null;
    private static ClientData clientData = null;
    private static String parser = null;
    private static String caseItem = null;
    private static PartialValidPageParser partialValidPageParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.PARTIAL;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testPartialValidPageParser1() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setRdt(1);

        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser2() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(3686);
        ubiEvent.setUrlQueryString("Portlet");

        caseItem = Constants.CASE2;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser3() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(451);
        ubiEvent.setUrlQueryString("LogBuyerRegistrationJSEvent");

        caseItem = Constants.CASE3;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser4() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setWebServer("sandbox.ebay.internet");

        caseItem = Constants.CASE4;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser5() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2588);
        ubiEvent.setApplicationPayload("cflgs=ebay");

        caseItem = Constants.CASE5;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser6() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(123);
        ubiEvent.setApplicationPayload("cflgs=ebay");

        caseItem = Constants.CASE6;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser7() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setApplicationPayload("cflgs=ebay");

        caseItem = Constants.CASE7;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser8() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setSqr("null");

        caseItem = Constants.CASE8;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser9() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(1468660);
        ubiEvent.setSiteId(0);
        ubiEvent.setWebServer("rover.ebay.com");

        caseItem = Constants.CASE9;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser10() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(1702440);
        ubiEvent.setWebServer("rover.ebay.haxi");

        caseItem = Constants.CASE10;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser11() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setApplicationPayload("an=ebay&av=ebay");

        caseItem = Constants.CASE11;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser12() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setApplicationPayload("in=ebay");

        caseItem = Constants.CASE12;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser13() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(5360);
        ubiEvent.setUrlQueryString("ebay_xhr=2");

        caseItem = Constants.CASE13;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser14() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setUrlQueryString("/_vti_bin");

        caseItem = Constants.CASE14;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser15() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setApplicationPayload("mr=ebay");
        ubiEvent.setUrlQueryString("?redirect=mobile");

        caseItem = Constants.CASE15;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser16() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2043141);
        ubiEvent.setUrlQueryString("/intercept.jsf");

        caseItem = Constants.CASE16;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPartialValidPageParser17() {
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2765);
        ubiEvent.setApplicationPayload("stateebay");

        caseItem = Constants.CASE17;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser18(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setUrlQueryString("_showdiag=1");

        caseItem = Constants.CASE18;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser19(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();
        clientData = new ClientData();

        clientData.setRemoteIP("10.2.137.50");

        ubiEvent.setClientData(clientData);
        ubiEvent.setUrlQueryString("_showdiag=");

        caseItem = Constants.CASE19;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser20(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setUrlQueryString("/&nbsb;");

        caseItem = Constants.CASE20;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser21(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setSqr("postalCodeTestQuery");
        ubiEvent.setPageId(1677950);

        caseItem = Constants.CASE21;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser22(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(5713);

        caseItem = Constants.CASE22;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser23(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();
        clientData = new ClientData();

        clientData.setAgent("eBayNioHttpClient");

        ubiEvent.setClientData(clientData);
        ubiEvent.setPageId(Integer.MIN_VALUE);

        caseItem = Constants.CASE23;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser24(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2050867);
        ubiEvent.setUrlQueryString("json");

        caseItem = Constants.CASE24;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser25(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2052122);
        ubiEvent.setUrlQueryString("json");

        caseItem = Constants.CASE25;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser26(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setUrlQueryString("null");

        caseItem = Constants.CASE26;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser27(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2050601);
        ubiEvent.setPageName("ebayFeedHome");

        caseItem = Constants.CASE27;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser28(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2054095);
        ubiEvent.setUrlQueryString("ebay/survey");

        caseItem = Constants.CASE28;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser29(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2056116);
        ubiEvent.setUrlQueryString("/itm/watchInline");

        caseItem = Constants.CASE29;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser30(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2059707);
        ubiEvent.setUrlQueryString("/itm/delivery");

        caseItem = Constants.CASE30;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser31(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2052197);
        ubiEvent.setUrlQueryString("ImportHubItemDescription");

        caseItem = Constants.CASE31;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser32(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2047935);
        ubiEvent.setWebServer("reco.ebay.haxi");

        caseItem = Constants.CASE32;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser33(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2067339);
        ubiEvent.setUrlQueryString("/roverimp/0/0/9?");

        caseItem = Constants.CASE33;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser34(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2056812);

        caseItem = Constants.CASE34;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser35(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        ubiEvent.setPageId(2056116);

        caseItem = Constants.CASE35;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser36(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();
        clientData = new ClientData();

        clientData.setAgent("ebayUserAgent/eBayIOS");

        ubiEvent.setClientData(clientData);
        ubiEvent.setPageId(2481888);
        ubiEvent.setAppId(3564);

        caseItem = Constants.CASE36;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }

    @Test
    public void testPatialValidPageParser37(){
        partialValidPageParser = new PartialValidPageParser();
        ubiEvent = new UbiEvent();

        caseItem = Constants.CASE37;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                partialValidPageParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateInteger(entry.getValue(), ubiEvent.getPartialValidPage()));
            }
        } catch (Exception e) {
            logger.error("partial test fail!!!");
        }
    }
}
