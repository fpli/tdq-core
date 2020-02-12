package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SignatureDetectable;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.parser.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.accumulators.AverageAccumulator;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DetectableMapFunction extends RichMapFunction< UbiEvent, SignatureDetectable> {

    @Override
    public void open(Configuration conf) throws Exception {

    }

    @Override
    public SignatureDetectable map(UbiEvent value) throws Exception {

        return (SignatureDetectable)value;
    }
}