package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class RheosEventSerializationSchema implements SerializationSchema<SojEvent> {

    @Override
    public byte[] serialize(SojEvent element) {
        return new byte[0];
    }
}
