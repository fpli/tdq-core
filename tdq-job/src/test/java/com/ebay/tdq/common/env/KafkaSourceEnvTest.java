package com.ebay.tdq.common.env;

import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
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
    val schema = new PathFinderRawEventKafkaDeserializationSchema(
        null, getTime("2021-07-20 18:25:00"), null);
    Assert.assertFalse(schema.isEndOfStream(getTime("2021-07-20 18:24:00")));
    Assert.assertTrue(schema.isEndOfStream(getTime("2021-07-20 18:26:00")));
  }

  @Test
  public void testIsProcessingElement() throws Exception {
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement1() throws Exception {
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
    long current = getTime("2021-07-20 18:24:59");
    Assert.assertTrue(tdqEnv.isProcessElement(current));
  }

  @Test
  public void testIsProcessingElement2() throws Exception {
    long start = getTime("2021-07-20 18:20:00");
    long end = getTime("2021-07-20 18:25:00");
    long current = getTime("2021-07-20 18:24:59");
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test"

    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
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
    TdqContext tdqContext = new TdqContext(new String[]{
        "--tdq-profile", "tdq-test"
    });
    final TdqEnv tdqEnv = tdqContext.getTdqEnv();
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
