package com.ebay.sojourner.ubd.operators.sessionizer;

import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UbiSessionReducer implements ReduceFunction<UbiEvent> {

    @Override
    public UbiEvent reduce(UbiEvent value1, UbiEvent value2) throws Exception {
        return null;
    }
}
