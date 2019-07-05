package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.ClientIPParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientIPParserTest {
    private static final Logger logger = Logger.getLogger(ClientIPParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static ClientIPParser clientIPParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser(){
        parser = Constants.CLIENTIP;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testClientIPParser1(){
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateString(entry.getValue(),ubiEvent.getClientIP()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser2() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser3() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser4() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser5() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE5;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("clientIP test fail!!!");
        }
    }
}
