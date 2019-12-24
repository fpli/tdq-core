package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.rt.util.AppEnv;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.GenericRecordDomainDataDecoder;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;

import java.util.HashMap;
import java.util.Map;

public class RheosEventSerdeFactory {
    private static final String RHEOS_SERVICES_URL = AppEnv.config().getRheos().getServiceUrl();
    private static RheosEventDeserializer rheosEventHeaderDeserializer;
    private static GenericRecordDomainDataDecoder rheosEventDeserializer;

    public static RheosEventDeserializer getRheosEventHeaderDeserializer() {
        if (rheosEventHeaderDeserializer == null) {
            rheosEventHeaderDeserializer = new RheosEventDeserializer();
        }
        return rheosEventHeaderDeserializer;
    }

    public static GenericRecordDomainDataDecoder getRheosEventDeserializer() {
        if (rheosEventDeserializer == null) {
            Map<String, Object> config = new HashMap<>();
            config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, RHEOS_SERVICES_URL);
            rheosEventDeserializer = new GenericRecordDomainDataDecoder(config);
        }
        return rheosEventDeserializer;
    }
}
