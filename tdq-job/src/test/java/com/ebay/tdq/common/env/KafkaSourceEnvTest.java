package com.ebay.tdq.common.env;

import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
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
    KafkaSourceConfig ksc = new KafkaSourceConfig();
    ksc.setName("test");
    ksc.setRheosServicesUrls(null);
    ksc.setOutOfOrderlessMs(0L);
    ksc.setToTimestamp(getTime("2021-07-20 18:25:00"));
    val schema = new PathFinderRawEventKafkaDeserializationSchema(ksc);
    Assert.assertFalse(schema.isEndOfStream(getTime("2021-07-20 18:24:00")));
    Assert.assertTrue(schema.isEndOfStream(getTime("2021-07-20 18:26:00")));
  }

  @Test
  public void testIsProcessingElement() throws Exception {
    final TdqEnv tdqEnv = new TdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement1() throws Exception {
    final TdqEnv tdqEnv = new TdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement2() throws Exception {
    long start = getTime("2021-07-20 18:20:00");
    long end = getTime("2021-07-20 18:25:00");
    long current = getTime("2021-07-20 18:24:59");
    final TdqEnv tdqEnv = new TdqEnv();
    tdqEnv.setFromTimestamp(start);
    tdqEnv.setToTimestamp(end);

    Assert.assertTrue(tdqEnv.isProcessElement(current));
    current = getTime("2021-07-20 18:25:59");
    Assert.assertFalse(tdqEnv.isProcessElement(current));
    current = getTime("2021-07-20 18:19:59");
    Assert.assertFalse(tdqEnv.isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement3() throws Exception {
    long end = getTime("2021-07-20 18:25:00");
    final TdqEnv tdqEnv = new TdqEnv();
    tdqEnv.setToTimestamp(end);

    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.isProcessElement(current));

    current = getTime("2021-07-20 18:25:59");
    Assert.assertFalse(tdqEnv.isProcessElement(current));
  }

  private long getTime(String s) throws ParseException {
    return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(s).getTime();
  }

}
