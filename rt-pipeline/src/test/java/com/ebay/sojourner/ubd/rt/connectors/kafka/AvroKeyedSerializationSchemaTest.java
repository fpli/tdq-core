package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import org.junit.Test;

public class AvroKeyedSerializationSchemaTest {

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
