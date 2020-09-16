package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppidParserSpeciaTest {

  private AppIdParser appIdParser;
  private UbiEvent ubiEvent;
  private RawEvent rawEvent;

  @BeforeEach
  public void setup() {
    appIdParser = new AppIdParser();
    ubiEvent = new UbiEvent();
    rawEvent = new RawEvent();
    Map<String, String> sojA = new HashMap<>();
    sojA.put("a", "b");
    Map<String, String> sojC = new HashMap<>();
    sojC.put("b", "b");
    Map<String, String> sojK = new HashMap<>();
    sojK.put("c", "b");
    ClientData clientData = new ClientData();
    clientData.setAgent("ebay");
    rawEvent.setClientData(clientData);
    rawEvent.setSojA(sojA);
    rawEvent.setSojC(sojC);
    rawEvent.setSojK(sojK);
  }

  @Test
  public void test_applicationPayload_is_not_null() {

    ubiEvent.setApplicationPayload("app=1234");
    appIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(1234, ubiEvent.getAppId());
  }

  @Test
  public void test_applicationPayload_is_null() {

    appIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertNull(ubiEvent.getAppId());
  }

  @Test
  public void test_applicationPayload_is_number() {

    ubiEvent.setApplicationPayload("app=123");
    appIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertEquals(123, ubiEvent.getAppId());
  }

  @Test
  public void test_applicationPayload_is_not_number() {

    ubiEvent.setApplicationPayload("app=ebay");
    appIdParser.parse(rawEvent, ubiEvent);
    Assertions.assertNull(ubiEvent.getAppId());
  }
}
