package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.common.env.TdqEnv;
import java.util.Random;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author juntzhang
 */
public abstract class AbstractSource {

  public abstract TdqEnv getTdqEnv();

  protected DataStream<RawEvent> sample(
      DataStream<RawEvent> rawEventDataStream,
      String slotSharingGroup,
      String id) {
    String uid = "src_sample_opt_" + id.toLowerCase();
    final double sampleFraction = getTdqEnv().getKafkaSourceEnv().getSrcSampleFraction();
    if (sampleFraction > 0 && sampleFraction < 1) {
      return rawEventDataStream
          .filter(r -> {
            return Math.abs(new Random().nextDouble()) < sampleFraction;
          })
          .name(uid)
          .uid(uid)
          .slotSharingGroup(slotSharingGroup)
          .setParallelism(rawEventDataStream.getParallelism());
    }
    return rawEventDataStream;
  }
}
