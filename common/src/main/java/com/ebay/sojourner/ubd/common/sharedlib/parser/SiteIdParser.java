package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IntegerField;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SiteIdParser implements FieldParser<RawEvent, UbiEvent> {
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
    public void init() throws Exception {
        // nothing to do
    }
}
