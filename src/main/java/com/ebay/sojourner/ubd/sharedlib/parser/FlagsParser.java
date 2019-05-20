package com.ebay.sojourner.ubd.sharedlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class FlagsParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    
    private static final Logger log = Logger.getLogger(FlagsParser.class);
    private static final String FLGS_TAG = "flgs";
    public void parse(RawEvent event, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();
        map.putAll(event.getSojA());
        map.putAll(event.getSojK());
        map.putAll(event.getSojC());
        String flags = map.get(FLGS_TAG);
        if (StringUtils.isNotBlank(flags)) {
            try {
                ubiEvent.setFlags(flags.trim());
            } catch (NumberFormatException e) {
                log.debug("Flag format is incorrect");
            }
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
