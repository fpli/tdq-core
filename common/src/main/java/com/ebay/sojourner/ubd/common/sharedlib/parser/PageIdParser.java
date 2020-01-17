package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IntegerField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageIdParser implements FieldParser<RawEvent, UbiEvent> {

    private static final String P_TAG = "p";

    public void parse(RawEvent event, UbiEvent ubiEvent) {
        try {
            Map<String, String> map = new HashMap<>();
            map.putAll(event.getSojA());
            map.putAll(event.getSojK());
            map.putAll(event.getSojC());
            String pageid = null;
            if (StringUtils.isNotBlank(map.get(P_TAG))) {
                pageid = map.get(P_TAG);
            }
            String value = IntegerField.parse(pageid);
            if (StringUtils.isNotBlank(pageid)) {
                ubiEvent.setPageId(Integer.parseInt(value));
            }
        } catch (NumberFormatException e) {
            log.warn("Parsing PageId failed, format incorrect...");
        }
    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
