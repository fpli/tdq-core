package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
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
    String guid = getString(genericRecord.get("guid"));
    Long eventTimestamp = getLong(genericRecord.get("eventTimestamp"));
    String sid = getString(genericRecord.get("sid"));
    Long eventCaptureTime = getLong(genericRecord.get("eventCaptureTime"));
    String requestCorrelationId = getString(genericRecord.get("requestCorrelationId"));
    String pageFamily = getString(genericRecord.get("pageFamily"));
    String remoteIP = getString(genericRecord.get("remoteIP"));
    String appVersion = getString(genericRecord.get("appVersion"));
    String eventFamily = getString(genericRecord.get("eventFamily"));
    String eventAction = getString(genericRecord.get("eventAction"));
    String trafficSource = getString(genericRecord.get("trafficSource"));
    String osVersion = getString(genericRecord.get("osVersion"));
    String deviceFamily = getString(genericRecord.get("deviceFamily"));
    String deviceType = getString(genericRecord.get("deviceType"));
    String browserVersion = getString(genericRecord.get("browserVersion"));
    String browserFamily = getString(genericRecord.get("browserFamily"));
    String osFamily = getString(genericRecord.get("osFamily"));
    String enrichedOsVersion = getString(genericRecord.get("enrichedOsVersion"));
    String rlogid = getString(genericRecord.get("rlogid"));

    return new JetStreamOutputEvent(guid, eventTimestamp, eventCreateTimestamp, sid,
        eventCaptureTime, requestCorrelationId, pageFamily, remoteIP, appVersion, eventFamily,
        eventAction, trafficSource, osVersion, deviceFamily, deviceType, browserVersion,
        browserFamily, osFamily, enrichedOsVersion, rlogid);
  }

  private Integer getInteger(Object o){
    if(StringUtils.isEmpty(getString(o))){
      return null;
    }else{
      return Integer.valueOf(getString(o));
    }
  }
  private boolean getBoolean(Object o){
    if(StringUtils.isEmpty(getString(o))){
      return false;
    }else{
      return Boolean.valueOf(getString(o));
    }
  }


  private Long getLong(Object o){
    if(StringUtils.isEmpty(getString(o))){
      return null;
    }else{
      return Long.valueOf(getString(o));
    }
  }

  private String getString(Object o) {
    return (o != null&&!"null".equals(o.toString())) ? o.toString() : null;
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
