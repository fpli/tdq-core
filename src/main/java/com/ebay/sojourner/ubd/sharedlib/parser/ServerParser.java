package com.ebay.sojourner.ubd.sharedlib.parser;


import com.ebay.sojourner.ubd.model.ClientData;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;


public class ServerParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {

    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        ClientData clientData = rawEvent.getClientData();
        if (clientData != null) {
            String serverInfo =clientData.getServer();
            if (StringUtils.isNotBlank(serverInfo)) {
                ubiEvent.setWebServer(serverInfo);
            }
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
