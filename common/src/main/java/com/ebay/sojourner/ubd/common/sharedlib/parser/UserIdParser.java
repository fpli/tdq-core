package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.IntegerField;
import com.ebay.sojourner.ubd.common.sharedlib.util.RegexReplace;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserIdParser implements FieldParser<RawEvent, UbiEvent> {
    private static final Logger log = Logger.getLogger(UserIdParser.class);
    private static final String U_TAG = "u";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String userId =null;
        if (StringUtils.isNotBlank(map.get(U_TAG))) {
            userId=map.get(U_TAG);
        }

        try {
            if (StringUtils.isNotBlank(userId)) {
                if (IntegerField.getIntVal(userId) == null) {
                    userId = RegexReplace.replace(userId, "(\\D)+", "", 1, 0, 'i');
                    if (userId.length() > 28) {
                        return;
                    }
                }
                
                long result = Long.parseLong(userId.trim());
                if (result >= 1 && result <= 9999999999999999L) {
                    ubiEvent.setUserId(String.valueOf(result));
                }
            }
        } catch (Exception e) {
            log.error("Incorrect format: " + userId);
        }
    }

    @Override
    public void init() throws Exception {
    }
}
