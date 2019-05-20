package com.ebay.sojourner.ubd.sharedlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

public class AgentInfoParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    @Override
    public void init(Configuration configuration,RuntimeContext context) throws Exception {

    }

    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        String agentInfo =rawEvent.getClientData().getAgent();
        if (StringUtils.isNotBlank(agentInfo)) {
            ubiEvent.setAgentInfo(agentInfo);
        }
    }
}
