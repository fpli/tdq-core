package com.ebay.sojourner.ubd.util.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.LkpFetcher;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

import java.util.Map;

public class FindingFlagParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        Integer pageId = ubiEvent.getPageId();
        Map<Integer, Integer> findingFlagMap = LkpFetcher.getFindingFlagMap();
        if (findingFlagMap.containsKey(pageId)) {
            ubiEvent.setBitVal(findingFlagMap.get(pageId));
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        LkpFetcher.loadFindingFlag(conf,runtimeContext);
    }
}
