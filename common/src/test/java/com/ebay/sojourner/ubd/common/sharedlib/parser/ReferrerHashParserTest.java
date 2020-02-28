package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.ParserConstants;
import com.ebay.sojourner.ubd.common.util.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.util.VaildateResult;
import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ReferrerHashParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static ReferrerHashParser referrerHashParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
    public static void initParser() {
        parser = ParserConstants.REFERRERHASH;
        map = YamlUtil.getInstance().loadFileMap(ParserConstants.FILEPATH);
    }

    @Test
    public void testReferrerHash() {
        referrerHashParser = new ReferrerHashParser();
        ubiEvent = new UbiEvent();
        caseItem = ParserConstants.CASE1;

        HashMap<RawEvent, Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
        for (Map.Entry<RawEvent, Object> entry : rawEventAndExpectResult.entrySet()) {
            referrerHashParser.parse(entry.getKey(), ubiEvent);
            System.out.println(VaildateResult.validateString(entry.getValue(), TypeTransUtil.LongToString(ubiEvent.getRefererHash())));
        }
    }
}