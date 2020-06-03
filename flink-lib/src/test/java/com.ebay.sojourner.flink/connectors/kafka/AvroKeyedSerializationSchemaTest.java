package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import org.junit.Ignore;
import org.junit.Test;

public class AvroKeyedSerializationSchemaTest {

  @Ignore
  @Test
  public void testSojEvent() {
    AvroKeyedSerializationSchema serializer = new AvroKeyedSerializationSchema(SojEvent.class,
        "guid");
    SojEvent e = new SojEvent();
    e.setGuid("aaa");
    serializer.serializeKey(e);
    serializer.serializeValue(e);
  }

  @Test
  public void testSojSession() {
    AvroKeyedSerializationSchema serializer = new AvroKeyedSerializationSchema(SojSession.class,
        "guid");
    SojSession s = new SojSession();
    s.setGuid("aaa");
    serializer.serializeKey(s);
    serializer.serializeValue(s);
  }
}
