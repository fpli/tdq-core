package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.model.TdqEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;

/**
 * @author juntzhang
 */
@Slf4j
public abstract class AbstractTdqEventDeserializationSchema {

  private transient Map<String, Counter> counterMap;
  private transient MetricGroup group;
  private transient long errorMsgCurrentTimeMillis = 0L;
  private final String name;

  public AbstractTdqEventDeserializationSchema(String name) {
    this.name = name;
  }

  public void open(MetricGroup group) {
    this.group = group;
    counterMap = new HashMap<>();
    this.group = group.addGroup("tdq").addGroup("src", name);
    errorMsgCurrentTimeMillis = 0L;
    log.info("open success");
  }

  public void inc(String key) {
    if (counterMap == null) {
      return;
    }
    Counter counter = counterMap.get(key);
    if (counter == null) {
      if (group == null) {
        return;
      }
      counter = group.counter(key);
      counterMap.put(key, counter);
    }
    counter.inc();
  }

  public abstract TdqEvent deserialize1(byte[] message);

  public TdqEvent deserialize0(byte[] message) {
    try {
      return deserialize1(message);
    } catch (Exception e) {
      inc("deserializeError");
      if ((System.currentTimeMillis() - errorMsgCurrentTimeMillis) > 10000) {
        if (message != null) {
          log.error("record hex string:{}", Hex.encodeHexString(message));
        }
        log.error(e.getMessage(), e);
        errorMsgCurrentTimeMillis = System.currentTimeMillis();
      }
      return null;
    }
  }

}
