package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.SqrParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SqrParserTest {
    private static final Logger logger = Logger.getLogger(SqrParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static SqrParser sqrParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.SQR;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testSqrParser() {
        sqrParser = new SqrParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                sqrParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getSqr()));
            }
        } catch (Exception e) {
            logger.error("sqr test fail!!!");
        }
    }
}
