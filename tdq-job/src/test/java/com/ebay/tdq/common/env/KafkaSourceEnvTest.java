package com.ebay.tdq.common.env;

import com.ebay.tdq.utils.TdqContext;
import java.text.ParseException;
import lombok.val;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
public class KafkaSourceEnvTest {

  @Test
  public void testIsEndOfStream() throws Exception {
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test",
        "--flink.app.source.end-timestamp", String.valueOf(getTime("2021-07-20 18:25:00")),
        "--flink.app.advance.watermark.out-of-orderless", "3min"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isEndOfStream(getTime("2021-07-20 18:24:00")));
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isEndOfStream(getTime("2021-07-20 18:26:00")));
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isEndOfStream(getTime("2021-07-20 18:28:00")));
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv().isEndOfStream(getTime("2021-07-20 18:28:01")));
  }

  @Test
  public void testIsProcessingElement() throws Exception {
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test",
        "--flink.app.source.from-timestamp", "earliest"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement1() throws Exception {
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test",
        "--flink.app.source.from-timestamp", "0"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement2() throws Exception {
    long start = getTime("2021-07-20 18:20:00");
    long end = getTime("2021-07-20 18:25:00");
    long current = getTime("2021-07-20 18:24:59");
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test",
        "--flink.app.source.from-timestamp", Long.toString(start),
        "--flink.app.source.end-timestamp", Long.toString(end)

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
    current = getTime("2021-07-20 18:25:59");
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
    current = getTime("2021-07-20 18:19:59");
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement3() throws Exception {
    long end = getTime("2021-07-20 18:25:00");
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test",
        "--flink.app.source.from-timestamp", "earliest",
        "--flink.app.source.end-timestamp", Long.toString(end)

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv().isProcessElement(current));

    current = getTime("2021-07-20 18:25:59");
    Assert.assertFalse(tdqEnv.getKafkaSourceEnv().isProcessElement(current));
  }

  private long getTime(String s) throws ParseException {
    return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(s).getTime();
  }

}
