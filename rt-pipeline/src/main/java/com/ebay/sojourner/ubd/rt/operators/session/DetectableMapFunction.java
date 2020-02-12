package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SignatureDetectable;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class DetectableMapFunction extends RichMapFunction<UbiSession, SignatureDetectable> {

    @Override
    public void open(Configuration conf) throws Exception {

    }

    @Override
    public SignatureDetectable map(UbiSession value) throws Exception {

        return (SignatureDetectable)value;
    }
}