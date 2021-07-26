package com.ebay.tdq.sources;

import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.TdqContext;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author juntzhang
 */
public class SourceBuilder {

  public static DataStream<TdqMetric> build(TdqConfig tdqConfig, TdqContext tdqCxt)
      throws ReflectiveOperationException {
    List<DataStream<TdqMetric>> list = new ArrayList<>();
    for (SourceConfig sourceConfig : tdqConfig.getSources()) {
      if (sourceConfig.getType().equals("realtime.kafka")) {
        list.add(RhsKafkaSourceBuilder.build(sourceConfig, tdqCxt));
      } else if (sourceConfig.getType().equals("realtime.memory")) {
        list.add(MemorySourceBuilder.build(sourceConfig, tdqCxt));
      } else {
        throw new IllegalArgumentException(sourceConfig.getType() + " not implement!");
      }
    }
    return list.stream().reduce(DataStream::union).orElse(null);
  }
}
