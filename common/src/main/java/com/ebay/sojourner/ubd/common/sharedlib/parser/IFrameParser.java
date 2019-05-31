package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IFrameParser implements FieldParser<RawEvent, UbiEvent> {

    private static final String P_TAG = "p";
    private static LkpFetcher lkpFetcher ;
    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.putAll(rawEvent.getSojA());
        map.putAll(rawEvent.getSojK());
        map.putAll(rawEvent.getSojC());
        String pageId =null;
        if (StringUtils.isNotBlank(map.get(P_TAG))) {
            pageId=map.get(P_TAG);
        }

        Set<String> pageIdSet = LkpFetcher.getInstance().getIframePageIdSet();

        if (pageIdSet.contains(pageId)) {
            ubiEvent.setIframe(1);
        } else {
            ubiEvent.setIframe(0);
        }

    }

    @Override
    public void init() throws Exception {


    }
}
