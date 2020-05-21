package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputSession;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojSessionDeserializationSchema implements
    DeserializationSchema<JetStreamOutputSession> {

  @Override
  public JetStreamOutputSession deserialize(byte[] message) throws IOException {
    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);
    return new JetStreamOutputSession();
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
  public boolean isEndOfStream(JetStreamOutputSession nextElement) {
    return false;
  }

  @Override
  public TypeInformation<JetStreamOutputSession> getProducedType() {
    return TypeInformation.of(JetStreamOutputSession.class);
  }
}
