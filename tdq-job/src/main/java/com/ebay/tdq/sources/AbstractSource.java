package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import java.util.Random;
import org.apache.flink.streaming.api.datastream.DataStream;

import static com.ebay.tdq.utils.TdqConstant.SRC_SAMPLE_FRACTION;

/**
 * @author juntzhang
 */
public abstract class AbstractSource {
  protected static DataStream<RawEvent> sample(
      DataStream<RawEvent> rawEventDataStream,
      String slotSharingGroup,
      String id,
      int parallelism) {
    String uid = "src_sample_operator_" + id.toLowerCase();
    if (SRC_SAMPLE_FRACTION > 0 && SRC_SAMPLE_FRACTION < 1) {
      final double sampleFraction = SRC_SAMPLE_FRACTION;
      return rawEventDataStream
          .filter(r -> {
            return Math.abs(new Random().nextDouble()) < sampleFraction;
          })
          .name(uid)
          .uid(uid)
          .slotSharingGroup(slotSharingGroup)
          .setParallelism(parallelism);
    }
    return rawEventDataStream;
  }
}
