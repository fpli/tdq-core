package com.ebay.tdq.common.model;

import com.ebay.sojourner.common.model.RawEvent;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import lombok.Data;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author juntzhang
 */
@Data
public class TdqEvent implements Serializable {

  private String type;
  private Map<String, Object> data;

  public TdqEvent() {
  }

  @VisibleForTesting
  public TdqEvent(Map<String, Object> record) {
    this.type = "GENERIC_EVENT";
    this.data = Maps.newHashMap(record);
  }

  public TdqEvent(GenericRecord record) {
    this.type = "GENERIC_EVENT";
    this.data = convert(record);
  }

  public TdqEvent buildEventTime(Long eventTimeMillis) {
    put("event_timestamp", eventTimeMillis * 1000);
    put("event_time_millis", eventTimeMillis);
    return this;
  }

  public TdqEvent(RawEvent sojEvent) {
    this.type = "SOJ_EVENT";
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

  public static Schema getFieldType0(Schema schema, String[] names) {
    if (schema == null || ArrayUtils.isEmpty(names)) {
      return null;
    }
    Schema v = schema;
    for (String k : names) {
      if (v.getType().equals(Type.UNION)) {
        for (Schema s : v.getTypes()) {
          if (s.getType().equals(Schema.Type.NULL)) {
            continue;
          }
          if (s.getType().equals(Type.MAP)) {
            v = s.getValueType();
          } else {
            Schema.Field f = s.getField(k);
            if (f != null) {
              v = f.schema();
              break;
            }
          }

        }
      } else {
        if (v.getType().equals(Type.MAP)) {
          v = v.getValueType();
        } else {
          Schema.Field f = v.getField(k);
          if (f != null) {
            v = f.schema();
          }
        }
      }
    }

    return v;
  }

  public static Schema getFieldType(Schema schema, String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    return getFieldType0(schema, name.split("\\."));
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


  public Object get(String[] keys) {
    if (ArrayUtils.isEmpty(keys)) {
      return null;
    }
    Object v = data;
    for (String k : keys) {
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

  public Object get(String key) {
    if (StringUtils.isBlank(key)) {
      return null;
    }
    return get(key.split("\\."));
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
        .add("eventTime='" + getEventTimeMsStr() + "'")
        .add("data=" + data)
        .toString();
  }
}
