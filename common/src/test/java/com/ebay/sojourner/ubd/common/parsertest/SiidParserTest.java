package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.SiidParser;
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

public class SiidParserTest {
    private static final Logger logger = Logger.getLogger(SiidParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static SiidParser siidParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.SIID;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testSiidParser1() {
        siidParser = new SiidParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siidParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
        }
    }

    @Test
    public void testSiidParser2() {
        siidParser = new SiidParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE2;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siidParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
        }
    }

    @Test
    public void testSiidParser3() {
        siidParser = new SiidParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE3;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siidParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
        }
    }

    @Test
    public void testSiidParser4() {
        siidParser = new SiidParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE4;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            siidParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.vaildateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getSourceImprId())));
        }
    }
}
