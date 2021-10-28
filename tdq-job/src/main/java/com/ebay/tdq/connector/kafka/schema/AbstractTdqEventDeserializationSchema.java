package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.model.TdqEvent;
import java.io.Serializable;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;

/**
 * @author juntzhang
 */
@Slf4j
public abstract class AbstractTdqEventDeserializationSchema implements Serializable {

  abstract Map<String, Counter> getCounterMap();

  abstract MetricGroup getGroup();

  abstract long getErrorMsgCurrentTimeMillis();

  abstract void setErrorMsgCurrentTimeMillis(Long t);


  public void inc(String key) {
    if (getCounterMap() == null) {
      return;
    }
    Counter counter = getCounterMap().get(key);
    if (counter == null) {
      if (getGroup() == null) {
        return;
      }
      counter = getGroup().counter(key);
      getCounterMap().put(key, counter);
    }
    counter.inc();
  }

  public abstract TdqEvent deserialize1(byte[] message);

  public TdqEvent deserialize0(byte[] message) {
    try {
      return deserialize1(message);
    } catch (Exception e) {
      inc("deserializeError");
      if ((System.currentTimeMillis() - getErrorMsgCurrentTimeMillis()) > 10000) {
        if (message != null) {
          log.error("record hex string:{}", Hex.encodeHexString(message));
        }
        log.error(e.getMessage(), e);
        setErrorMsgCurrentTimeMillis(System.currentTimeMillis());
      }
      return null;
    }
  }

}
