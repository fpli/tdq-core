package com.ebay.sojourner.ubd.sojlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.sojlib.util.IntegerField;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SiteIdParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(SiteIdParser.class);
    private static final String T_TAG = "t";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        try {
            String siteId =null;
            Map<String, String> map = new HashMap<>();
            map.putAll(rawEvent.getSojA());
            map.putAll(rawEvent.getSojK());
            map.putAll(rawEvent.getSojC());
            if (StringUtils.isNotBlank(map.get(T_TAG))) {
                siteId =map.get(T_TAG);
            }
            siteId = IntegerField.parse(siteId);
            if (StringUtils.isNotBlank(siteId)) {
                ubiEvent.setSiteId(Integer.parseInt(siteId));
            }
        } catch (Exception e) {
            log.debug("Parsing SiteId failed, format wrong...");
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
