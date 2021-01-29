package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojEvent;

import java.io.IOException;

import com.ebay.sojourner.common.util.SojTimestamp;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BinaryToSojEventMapFunction extends RichMapFunction<byte[], SojEvent> {

    private transient DatumReader<SojEvent> reader;

    @Override
    public SojEvent map(byte[] data) throws Exception {
        if (reader == null) {
            reader = new SpecificDatumReader<>(SojEvent.class);
        }

        Decoder decoder = null;
        try {
            decoder = DecoderFactory.get().binaryDecoder(data, null);
            SojEvent sojEvent = reader.read(null, decoder);
            if (sojEvent.getEventTimestamp() != null) {
                sojEvent.setEventTimestamp(
                        SojTimestamp.getSojTimestamp(sojEvent.getEventTimestamp()));
            }
            return sojEvent;
        } catch (IOException e) {
            throw new RuntimeException("Deserialize SojEvent error", e);
        }
    }
}
