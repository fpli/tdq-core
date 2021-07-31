package com.ebay.tdq.common.model;

import com.ebay.sojourner.common.model.RawEvent;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author juntzhang
 */
public class TdqEvent implements Serializable {

  public String type;
  public String eventTimeFiled;
  public Map<String, Object> data;

  public TdqEvent(GenericRecord record, String eventTimeFiled) {
    this.type = "GENERIC_EVENT";
    this.eventTimeFiled = eventTimeFiled;
    this.data = convert(record);

    long eventTimeMillis = getOriginalEventTimeMs();
    put("event_timestamp", eventTimeMillis * 1000);
    put("event_time_millis", eventTimeMillis);
  }

  public TdqEvent(RawEvent sojEvent) {
    this(sojEvent, "eventTimestamp");
  }

  public TdqEvent(RawEvent sojEvent, String eventTimeFiled) {
    this.type = "SOJ_EVENT";
    this.eventTimeFiled = eventTimeFiled;
    this.data = new HashMap<>();
    put("sojA", sojEvent.getSojA());
    put("sojC", sojEvent.getSojC());
    put("sojK", sojEvent.getSojK());
    put("eventTimestamp", sojEvent.getEventTimestamp());
    put("ingestTimestamp", sojEvent.getIngestTime());
    put("clientData", sojEvent.getClientData().getMap());

    put("rheosHeader", convert(sojEvent.getRheosHeader()));
    put("soj_timestamp", sojEvent.getEventTimestamp());

    long eventTimeMillis = sojEvent.getUnixEventTimestamp();
    put("event_timestamp", eventTimeMillis * 1000);
    put("event_time_millis", eventTimeMillis);
  }

  public static Schema getField(Schema schema, String name) {
    if (schema == null || StringUtils.isBlank(name)) {
      return null;
    }
    Schema v = schema;
    for (String k : name.split("\\.")) {
      if (v.getType().equals(Type.MAP)) {
        return v.getValueType();
      }
      Schema.Field f = v.getField(k);
      if (f != null) {
        v = f.schema();
      }
    }
    return v;
  }

  private long getOriginalEventTimeMs() {
    return (long) get(eventTimeFiled);
  }

  public long getEventTimeMs() {
    return (long) get("event_time_millis");
  }

  @VisibleForTesting
  public void remote(String field) {
    this.data.remove(field);
  }

  public Map<String, Object> convert(Map<?, ?> record) {
    Map<String, Object> map = new HashMap<>();
    for (Map.Entry<?, ?> e : record.entrySet()) {
      // map key only support string
      String key = e.getKey().toString();
      if (e.getValue() instanceof Utf8) {
        map.put(key, convert((Utf8) e.getValue()));
      } else if (e.getValue() instanceof GenericRecord) {
        map.put(key, convert((GenericRecord) e.getValue()));
      } else if (e.getValue() instanceof Map) {
        map.put(key, convert((Map<?, ?>) e.getValue()));
      } else {
        map.put(key, e.getValue());
      }
    }
    return map;
  }

  public Map<String, Object> convert(GenericRecord record) {
    Map<String, Object> ans = new HashMap<>();
    if (record == null) {
      return null;
    }
    for (Schema.Field field : record.getSchema().getFields()) {
      Object o = record.get(field.pos());
      if (o instanceof Utf8) {
        ans.put(field.name(), convert((Utf8) o));
      } else if (o instanceof GenericRecord) {
        ans.put(field.name(), convert((GenericRecord) o));
      } else if (o instanceof Map) {
        ans.put(field.name(), convert((Map<?, ?>) o));
      } else {
        ans.put(field.name(), o);
      }
    }
    return ans;
  }

  public String convert(Utf8 utf8) {
    if (utf8 == null) {
      return null;
    }
    return utf8.toString();
  }


  public Object get(String keys) {
    if (StringUtils.isBlank(keys)) {
      return null;
    }
    Object v = data;
    for (String k : keys.split("\\.")) {
      if (v == null) {
        return null;
      } else if (v instanceof Map) {
        v = ((Map<?, ?>) v).get(k);
      } else if (v instanceof GenericRecord) {
        v = ((GenericRecord) v).get(k);
      }
    }
    return v;
  }

  public TdqEvent put(String key, Object v) {
    data.put(key, v);
    return this;
  }

  public String getEventTimeMsStr() {
    return DateFormatUtils.format(getEventTimeMs(), "yyyy-MM-dd HH:mm:ss");
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TdqEvent.class.getSimpleName() + "[", "]")
        .add("type='" + type + "'")
        .add("eventTimeFiled='" + eventTimeFiled + "'")
        .add("data=" + data)
        .toString();
  }
}
