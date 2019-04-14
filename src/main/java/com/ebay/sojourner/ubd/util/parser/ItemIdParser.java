package com.ebay.sojourner.ubd.util.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.sojlib.IntegerField;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ItemIdParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {

    private static final Logger log = Logger.getLogger(ItemIdParser.class);
    private static final String _ITM_TAG = "_itm";
    private static final String ITM_TAG = "itm";
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String itemId1 =null;
        String itemId2 =null;
        if (StringUtils.isNotBlank(map.get(_ITM_TAG))) {
            itemId1 =map.get(_ITM_TAG);
        }
        if (StringUtils.isNotBlank(map.get(ITM_TAG))) {
            itemId2 = map.get(ITM_TAG);
        }
        Long itemId = null;
        if (IntegerField.getIntVal(itemId1) != null) {
            try {
                itemId = Long.parseLong(itemId1.trim());
            } catch (Exception e) {
                log.debug("Parsing ItemId failed, format incorrect: " + itemId1);
            }
        } else {
            if (IntegerField.getIntVal(itemId2) != null) {
                try {
                    itemId = Long.parseLong(itemId2.trim());
                } catch (Exception e) {
                    log.debug("Parsing ItemId failed, format incorrect: " + itemId2);
                }
            }
        }

        if (itemId != null && itemId < 999999999999999999L) {
            ubiEvent.setItemId(itemId);
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        // nothing to do
    }
}
