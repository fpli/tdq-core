package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.ClientIPParser;
import com.ebay.sojourner.ubd.common.util.ParserConstants;
import com.ebay.sojourner.ubd.common.util.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.util.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClientIPParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static ClientIPParser clientIPParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser(){
        parser = ParserConstants.CLIENTIP;
        map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
    }

    @Test
    public void testClientIPParser1(){
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getClientIP()));
            }
        } catch (Exception e) {
            log.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser2() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE2;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            log.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser3() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE3;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            log.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser4() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE4;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            log.error("clientIP test fail!!!");
        }
    }

    @Test
    public void testClientIPParser5() {
        clientIPParser = new ClientIPParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE5;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                clientIPParser.parse(entry.getKey(), ubiEvent);
                if(StringUtils.isBlank(ubiEvent.getClientIP())){
                    System.out.println("true");
                }
            }
        } catch (Exception e) {
            log.error("clientIP test fail!!!");
        }
    }
}
