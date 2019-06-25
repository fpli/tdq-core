package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ReguParser implements FieldParser<RawEvent, UbiEvent> {
    private static final Logger log = Logger.getLogger(ReguParser.class);
    
    public static final String REGU = "regU";

    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws RuntimeException {
        try {
            Map<String, String> map = new HashMap<>();
            map.putAll(rawEvent.getSojA());
            map.putAll(rawEvent.getSojK());
            map.putAll(rawEvent.getSojC());
            String regu =null;
            if (StringUtils.isNotBlank(map.get(REGU))) {
                regu = map.get(REGU);

            }
           // String regu = SOJNVL.getTagValue(rawEvent.getApplicationPayload(), REGU);
            if (StringUtils.isNotBlank(regu)) {
                ubiEvent.setRegu(1);
            } else {
                ubiEvent.setRegu(0);
            }
        } catch (Exception e) {
            log.debug("Parsing regu failed, format incorrect");
            ubiEvent.setRegu(0);
        }
    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
