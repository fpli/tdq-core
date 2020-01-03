package com.ebay.sojourner.ubd.common.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.AgentInfoParser;
import com.ebay.sojourner.ubd.common.sharelib.Constants;
import com.ebay.sojourner.ubd.common.sharelib.LoadRawEventAndExpect;
import com.ebay.sojourner.ubd.common.sharelib.VaildateResult;
import com.ebay.sojourner.ubd.common.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AgentInfoParserTest {

    private static UbiEvent ubiEvent = null;
    private static String parser = null;
    private static String caseItem = null;
    private static AgentInfoParser agentInfoParser = null;
    private static HashMap<String, Object> map = null;

    @BeforeAll
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
                System.out.println(VaildateResult.validateString(entry.getValue(), ubiEvent.getAgentInfo()));
            }
        }catch (Exception e){
            log.error("agent test fail!!!");
        }
    }
}
