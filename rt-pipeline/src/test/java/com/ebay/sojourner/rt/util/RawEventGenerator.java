package com.ebay.sojourner.rt.util;

import com.ebay.sojourner.common.model.RawEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RawEventGenerator {

  public static List<RawEvent> getRawEventList(String fileName) throws IOException {
    InputStream resourceAsStream = RawEventGenerator.class.getResourceAsStream(fileName);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resourceAsStream, new TypeReference<List<RawEvent>>() {});
  }
}
