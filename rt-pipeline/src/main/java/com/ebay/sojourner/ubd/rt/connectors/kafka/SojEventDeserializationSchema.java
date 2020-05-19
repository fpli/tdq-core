package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojEventDeserializationSchema implements DeserializationSchema<JetStreamOutputEvent> {

  @Override
  public JetStreamOutputEvent deserialize(byte[] message) throws IOException {
    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    long eventCreateTimestamp = rheosEvent.getEventCreateTimestamp();
    String guid = String.valueOf(genericRecord.get("guid"));
    Long eventTimestamp = (Long) genericRecord.get("eventTimestamp");
    String sid = String.valueOf(genericRecord.get("sid"));
    Long eventCaptureTime = (Long) genericRecord.get("eventCaptureTime");
    String requestCorrelationId = String.valueOf(genericRecord.get("requestCorrelationId"));
    String pageFamily = String.valueOf(genericRecord.get("pageFamily"));
    String remoteIP = String.valueOf(genericRecord.get("remoteIP"));
    String appVersion = String.valueOf(genericRecord.get("appVersion"));
    String eventFamily = String.valueOf(genericRecord.get("eventFamily"));
    String eventAction = String.valueOf(genericRecord.get("eventAction"));
    String trafficSource = String.valueOf(genericRecord.get("trafficSource"));
    String osVersion = String.valueOf(genericRecord.get("osVersion"));
    String deviceFamily = String.valueOf(genericRecord.get("deviceFamily"));
    String deviceType = String.valueOf(genericRecord.get("deviceType"));
    String browserVersion = String.valueOf(genericRecord.get("browserVersion"));
    String browserFamily = String.valueOf(genericRecord.get("browserFamily"));
    String osFamily = String.valueOf(genericRecord.get("osFamily"));
    String enrichedOsVersion = String.valueOf(genericRecord.get("enrichedOsVersion"));
    String rlogid = String.valueOf(genericRecord.get("rlogid"));

    return new JetStreamOutputEvent(guid, eventTimestamp, eventCreateTimestamp, sid,
        eventCaptureTime, requestCorrelationId, pageFamily, remoteIP, appVersion, eventFamily,
        eventAction, trafficSource, osVersion, deviceFamily, deviceType, browserVersion,
        browserFamily, osFamily, enrichedOsVersion, rlogid);
  }

  @Override
  public boolean isEndOfStream(JetStreamOutputEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<JetStreamOutputEvent> getProducedType() {
    return TypeInformation.of(JetStreamOutputEvent.class);
  }
}
