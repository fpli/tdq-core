package com.ebay.tdq.functions;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.HashMap;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author juntzhang
 */
@Slf4j
public class RawEventSourceMockFunction implements SourceFunction<RawEvent> {
  public static int getInt() {
    return Math.abs(new Random().nextInt());
  }

  public static String getItm() {
    return new String[]{"123", "1abc", "", null}[getInt() % 4];
  }

  public static int getSiteId() {
    return new int[]{1, 2, 3, 4}[getInt() % 1];
  }

  @Override
  public void run(SourceContext<RawEvent> ctx) {
    while (true) {
      String siteId = String.valueOf(getSiteId());
      String item = getItm();
      String tDuration = String.valueOf(getInt() % 100);
      String pageId = new String[]{"711", "1702898", "1677718"}[getInt() % 2];
      String contentLength = String.valueOf(getInt() % 100);
      RawEvent rawEvent = new RawEvent();
      rawEvent.setClientData(new ClientData());
      rawEvent.getClientData().setContentLength(contentLength);
      // rawEvent.setEventTimestamp(System.currentTimeMillis());
      rawEvent.setEventTimestamp(SojTimestamp.getSojTimestamp(System.currentTimeMillis()));
      rawEvent.setSojA(new HashMap<>());
      rawEvent.setSojK(new HashMap<>());
      rawEvent.setSojC(new HashMap<>());
      rawEvent.getSojA().put("p", pageId);
      rawEvent.getSojA().put("t", siteId);
      rawEvent.getSojA().put("TDuration", new String[]{tDuration, tDuration, "", "1bc", tDuration}[getInt() % 5]);
      rawEvent.getSojA().put("itm", item);
      ctx.collect(rawEvent);
      //      try {
      //        Thread.sleep(1);
      //        //        Thread.sleep(500 * (getInt() % 5 + 1));
      //      } catch (InterruptedException e) {
      //        log.error(e.getMessage(), e);
      //      }
    }
  }

  @Override
  public void cancel() {
  }
}
