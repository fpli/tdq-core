package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.RefererParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class RefererParserTest {
    private static final Logger logger = Logger.getLogger(RefererParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static RefererParser refererParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = Constants.REFERERPARSER;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testRefererParser() {
        refererParser = new RefererParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        try {
            HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
                refererParser.parse(entry.getKey(), ubiEvent);
                System.out.println(VaildateResult.validateString(entry.getValue(),ubiEvent.getReferrer()));
            }
        } catch (Exception e) {
            logger.error("referer test fail!!!");
        }
    }
}
