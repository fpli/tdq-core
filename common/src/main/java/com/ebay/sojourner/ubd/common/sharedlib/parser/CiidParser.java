package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJBase64ToLong;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJURLDecodeEscape;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CiidParser implements FieldParser<RawEvent, UbiEvent> {
    @Override
    public void init() throws Exception {

    }

    private static final Logger log = Logger.getLogger(CiidParser.class);
    private static final String CIID_TAG = "ciid";
    public void parse(RawEvent event, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();
        map.putAll(event.getSojA());
        map.putAll(event.getSojK());
        map.putAll(event.getSojC());
        String ciid =null;
        if (StringUtils.isNotBlank(map.get(CIID_TAG))) {
            ciid = map.get(CIID_TAG);

        }
        Long result = null;
        if (StringUtils.isNotBlank(ciid)) {
            try {
                result = SOJBase64ToLong.getLong(SOJURLDecodeEscape.decodeEscapes(ciid.trim(), '%'));
                if (result != null) {
                    ubiEvent.setCurrentImprId(result);
                }
            } catch (Exception e) {
                log.debug("Parsing Ciid failed, format incorrect: " + ciid);
            }
        }
    }


}
