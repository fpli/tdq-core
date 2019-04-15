package com.ebay.sojourner.ubd.operators.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sojlib.IntegerField;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PageIdParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {

    private static final Logger log = Logger.getLogger(PageIdParser.class);

    private static final String P_TAG = "p";
    public void parse(RawEvent event, UbiEvent ubiEvent) {
        try {
            Map<String, String> map = new HashMap<>();
            map.putAll(event.getSojA());
            map.putAll(event.getSojK());
            map.putAll(event.getSojC());
            String pageid =null;
            if (StringUtils.isNotBlank(map.get(P_TAG))) {
                pageid=map.get(P_TAG);
            }
            String value = IntegerField.parse(pageid);
            if (StringUtils.isNotBlank(pageid)) {
                ubiEvent.setPageId(Integer.parseInt(value));
            }
        } catch (NumberFormatException e) {
            log.debug("Parsing PageId failed, format incorrect...");

        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
