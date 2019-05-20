package com.ebay.sojourner.ubd.sojlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.PropertyUtils;
import com.ebay.sojourner.ubd.sojlib.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class RdtParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    
    private static final Logger log = Logger.getLogger(RdtParser.class);
    
    public static final String RDT = "rdt";
    
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
        String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
        String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
        String applicationPayload = null;
        if (mARecString != null) {
            applicationPayload = mARecString;
        }
        if ((applicationPayload != null) && (mKRecString != null)) {
            applicationPayload = applicationPayload + "&" + mKRecString;
        }

        // else set C record
        if (applicationPayload == null)
            applicationPayload = mCRecString;

        String payload = applicationPayload;
        try {
            if (StringUtils.isNotBlank(SOJNVL.getTagValue(payload, RDT))){
                if("0".equals(SOJNVL.getTagValue(payload, RDT))) {
                    ubiEvent.setRdt(0);
                }
                else {
                    ubiEvent.setRdt(1);
                }
            } else {
                ubiEvent.setRdt(0);
            }
        } catch (Exception e) {
            log.error("Parsing rdt failed, format wrong...");
            ubiEvent.setRdt(0);
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
