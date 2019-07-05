package com.ebay.sojourner.ubd.common.parsertest;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.AgentInfoParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class AgentInfoParserTest {
    private static final Logger logger = Logger.getLogger(AgentInfoParserTest.class);

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static AgentInfoParser agentInfoParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeClass
    public static void initParser() {
        parser = Constants.AGENTINFO;
        map = YamlUtil.getInstance().loadFileMap(Constants.FILEPATH);
    }

    @Test
    public void testAgentInfoParser() {
        agentInfoParser = new AgentInfoParser();
        ubiEvent = new UbiEvent();
        caseItem = Constants.CASE1;

        try{
            HashMap<RawEvent,Object> rawEventAndExpectResult = LoadRawEventAndExpect.getRawEventAndExpect(map, parser, caseItem);
            for(Map.Entry<RawEvent,Object> entry:rawEventAndExpectResult.entrySet()){
                agentInfoParser.parse(entry.getKey(),ubiEvent);
                System.out.println(VaildateResult.vaildateString(entry.getValue(), ubiEvent.getAgentInfo()));
            }
        }catch (Exception e){
            logger.error("agent test fail!!!");
        }
    }
}
