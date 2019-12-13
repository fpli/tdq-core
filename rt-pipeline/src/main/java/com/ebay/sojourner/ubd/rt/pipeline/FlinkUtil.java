package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.AttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class FlinkUtil {
    public static void main(String[] args) {
        TypeInformation<UbiSession> t1 = TypeExtractor.createTypeInfo(UbiSession.class);
        System.out.println(t1);
        TypeInformation<AttributeAccumulator> t2 = TypeExtractor.createTypeInfo(AttributeAccumulator.class);
        System.out.println(t2);
    }
}
