package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;


public class RefererParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    
    public static final String REFERER = "Referer";

    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        String clientData = rawEvent.getClientData().getReferrer();
        if (StringUtils.isNotBlank(clientData)) {
            ubiEvent.setReferrer(clientData);
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
