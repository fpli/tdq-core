package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.FindingFlagParser;
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

public class FindingFlagParserTest {
    private static final Logger logger = Logger.getLogger(FindingFlagParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static FindingFlagParser findingFlagParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.FINDINGFLAG;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testFindingFlagParser1() {
        findingFlagParser = new FindingFlagParser();
        ubiEvent = new UbiEvent(); // special value
        ubiEvent.setPageId(3141);
        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                findingFlagParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateInteger(entry.getValue(), ubiEvent.getBitVal()));
            }
        } catch (Exception e) {
            logger.error("findingflag test fail!!!");
        }
    }

    @Test
    public void testFindingFlagParser2() {
        findingFlagParser = new FindingFlagParser();
        ubiEvent = new UbiEvent();
        ubiEvent.setPageId(123);
        caseItem = Constants.CASE2;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                findingFlagParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.vaildateString(entry.getValue(), TypeTransUtil.IntegerToString(ubiEvent.getBitVal())));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("findingflag test fail!!!");
        }
    }
}
