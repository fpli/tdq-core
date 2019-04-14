package com.ebay.sojourner.ubd.util.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.LkpFetcher;
import com.ebay.sojourner.ubd.util.sojlib.SOJGetPageType;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.Map;

public class StaticPageTypeParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(StaticPageTypeParser.class);
    
    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        int result = 0;
        try {
            Long itemId = ubiEvent.getItemId();
            String flags = ubiEvent.getFlags();
            Integer rdt = ubiEvent.getRdt();
            Integer pageId = ubiEvent.getPageId();
            Integer[] pageInfo = new Integer[2];
            Map<Integer, Integer[]> vtNewIdsMap = LkpFetcher.getVtNewIdsMap();
            if (pageId != null && rdt != null) {
                if (vtNewIdsMap.containsKey(pageId)) {
                    pageInfo = vtNewIdsMap.get(pageId);
                }
                String itemid;
                if (itemId == null) {
                    itemid = null;
                } else {
                    itemid = String.valueOf(itemId);
                }
                result = SOJGetPageType.soj_get_page_type(itemid, flags, rdt.intValue(), pageId.intValue(), pageInfo[0], pageInfo[1]);
            } else {
                result = -1;
            }
            ubiEvent.setStaticPageType(result);
        } catch (Exception e) {
            log.error("Parse static page type error: " + e.getMessage());
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        LkpFetcher.loadVtNewIds(conf,runtimeContext);
    }
}
