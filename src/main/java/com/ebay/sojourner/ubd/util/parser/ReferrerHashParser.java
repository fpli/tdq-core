package com.ebay.sojourner.ubd.util.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ReferrerHashParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(ReferrerHashParser.class);
    private static final String R_TAG = "r";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        Long result = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.putAll(rawEvent.getSojA());
            map.putAll(rawEvent.getSojK());
            map.putAll(rawEvent.getSojC());
            String reffererHash =null;
            if (StringUtils.isNotBlank(map.get(R_TAG))) {
                reffererHash=map.get(R_TAG);
            }

            if (StringUtils.isNotBlank(reffererHash)) {
                result = Long.parseLong(reffererHash);
                if (result < 999999999999999999L) {
                    ubiEvent.setRefererHash(result);
                }
            }
        } catch (NumberFormatException e) {
            log.debug("Parsing ReffererHash failed, format incorrect...");
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
