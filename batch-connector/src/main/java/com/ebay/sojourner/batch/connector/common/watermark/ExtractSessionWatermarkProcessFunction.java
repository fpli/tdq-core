package com.ebay.sojourner.batch.connector.common.watermark;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.SojWatermark;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class ExtractSessionWatermarkProcessFunction extends
    ProcessFunction<SojSession, SojWatermark> {

  private AtomicInteger atomicInteger;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    atomicInteger = new AtomicInteger(0);
  }

  @Override
  public void processElement(SojSession value, Context ctx, Collector<SojWatermark> out)
      throws Exception {

    int andIncrement = atomicInteger.getAndIncrement();
    if (andIncrement % 1000 == 0) {
      out.collect(new SojWatermark(ctx.timestamp()));
    }
  }

  @Override
  public void close() throws Exception {
    super.close();
  }

}
