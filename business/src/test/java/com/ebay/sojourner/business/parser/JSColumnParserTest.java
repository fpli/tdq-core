package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;

public class JSColumnParserTest {

  private JSColumnParser jsColumnParser;
  private RawEvent rawEvent;
  private UbiEvent ubiEvent;
  private Map<String, String> sojA;
  private Map<String, String> sojK;

  @BeforeEach
  public void setup() {
    jsColumnParser = new JSColumnParser();
    rawEvent = new RawEvent();
    ubiEvent = new UbiEvent();
    sojA = new HashMap<>();
    sojK = new HashMap<>();
  }
}
