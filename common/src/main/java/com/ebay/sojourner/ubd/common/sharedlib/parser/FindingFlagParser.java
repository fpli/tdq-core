package com.ebay.sojourner.ubd.common.sharedlib.parser;


import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

import java.util.Map;

public class FindingFlagParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private  static LkpFetcher lkpFetcher;
    @Override
    public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
        Integer pageId = ubiEvent.getPageId();
        Map<Integer, Integer> findingFlagMap = lkpFetcher.getFindingFlagMap();
        if (findingFlagMap.containsKey(pageId)) {
            ubiEvent.setBitVal(findingFlagMap.get(pageId));
        }
    }

    @Override
    public void init(Configuration conf,RuntimeContext runtimeContext) throws Exception {
        lkpFetcher = LkpFetcher.getInstance();
        lkpFetcher.loadFindingFlag();
    }
}
