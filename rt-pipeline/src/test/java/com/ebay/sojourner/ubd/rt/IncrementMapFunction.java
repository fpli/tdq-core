package com.ebay.sojourner.ubd.rt;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class IncrementMapFunction implements MapFunction<Tuple2<String,Integer>, Integer> {


    @Override
    public Integer map( Tuple2<String,Integer> record) throws Exception {
        return record.f1 + 1;
    }
}