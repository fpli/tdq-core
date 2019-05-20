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

public class ClickIdParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {


    private static final Logger log = Logger.getLogger(ClickIdParser.class);
    private static final String C_TAG = "c";
    @Override
    public void init(Configuration configuration,RuntimeContext context) throws Exception {

    }
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        String clickId =null;
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        if (StringUtils.isNotBlank(map.get(C_TAG))) {
            clickId=map.get(C_TAG);
        }
        long result = 0;
        try {
            String clickValue = IntegerField.parse(clickId);
            if (StringUtils.isNotBlank(clickValue)) {
                result = Long.parseLong(clickValue);
                result = result % 65536;
                if (result > 32767) {
                    result -= 65536;
                }
                ubiEvent.setClickId((int)result);
            }
        } catch (NumberFormatException e) {
            log.debug("Parsing ClickId failed, format incorrect: " + clickId);
        }
    }


}
