package com.ebay.sojourner.ubd.rt.operators.event;

import com.alibaba.fastjson.JSON;
import com.couchbase.client.java.document.json.JsonArray;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.parser.*;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class RawEvent2JSONMapFunction extends RichMapFunction<RawEvent,String> {


    @Override
    public void open(Configuration conf) throws Exception {

    }

    @Override
    public String map(RawEvent rawEvent) throws Exception {

        return JSON.toJSONString(rawEvent);

    }
}