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
    if (SRC_SAMPLE_FRACTION > 0 && SRC_SAMPLE_FRACTION < 1) {
      return rawEventDataStream
          .filter(r -> {
            return Math.abs(new Random().nextDouble()) < SRC_SAMPLE_FRACTION;
          })
          .name("Src Sample Operator " + id)
          .uid("src-sample-" + id.toLowerCase() + "-operator")
          .slotSharingGroup(slotSharingGroup)
          .setParallelism(parallelism);
    }
    return rawEventDataStream;
  }
}
