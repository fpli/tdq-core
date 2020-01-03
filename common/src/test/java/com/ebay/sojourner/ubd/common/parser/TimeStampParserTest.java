package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.TimestampParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TimeStampParserTest {
    private static final Logger logger = Logger.getLogger(TimeStampParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static TimestampParser timestampParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.TIMESTAMP;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testTimeStampParser1() {
        timestampParser = new TimestampParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                timestampParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getEventTimestamp())));
            }
        }catch (Exception e){
            logger.error("timestamp test fail!!!");
        }
    }

    @Test
    public void testTimeStampParser2() {
        timestampParser = new TimestampParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                timestampParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getEventTimestamp())));
            }
        }catch (Exception e){
            logger.error("timestamp test fail!!!");
        }
    }

    @Test
    public void testTimeStampParser3() {
        timestampParser = new TimestampParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                timestampParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getEventTimestamp())));
            }
        }catch (Exception e){
            logger.error("timestamp test fail!!!");
        }
    }
}
