package com.ebay.sojourner.ubd.sojlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.sojlib.util.SOJBase64ToLong;
import com.ebay.sojourner.ubd.sojlib.util.SOJURLDecodeEscape;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SiidParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(SiidParser.class);
    private static final String SIID_TAG = "siid";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        String siid = null;
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        if (StringUtils.isNotBlank(map.get(SIID_TAG))) {
            siid=map.get(SIID_TAG);
        }
        try {
            if (siid != null) {
                String decodeSiid = SOJURLDecodeEscape.decodeEscapes(siid.trim(), '%');
                if (StringUtils.isNotBlank(decodeSiid)) {
                    ubiEvent.setSourceImprId(SOJBase64ToLong.getLong(decodeSiid));
                }
            }
        } catch (Exception e) {
            log.error("Parsing Ciid failed, the siid value is: " + siid);
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
