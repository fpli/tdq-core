package com.ebay.sojourner.ubd.serialization;

import com.ebay.sojourner.ubd.model.SojEvent;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class RheosEventDeserializationSchema implements DeserializationSchema<SojEvent> {

    @Override
    public SojEvent deserialize(byte[] message) throws IOException {
        return null;
    }

    @Override
    public boolean isEndOfStream(SojEvent nextElement) {
        return false;
    }

    @Override
    public TypeInformation<SojEvent> getProducedType() {
        return null;
    }
}
