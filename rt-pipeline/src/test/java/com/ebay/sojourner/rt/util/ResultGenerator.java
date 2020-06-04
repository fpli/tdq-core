package com.ebay.sojourner.rt.util;

import com.ebay.sojourner.common.model.UbiSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResultGenerator {

  public static List<UbiSession> getUbiSessionList(String fileName) throws IOException {
    InputStream resourceAsStream = RawEventGenerator.class.getResourceAsStream(fileName);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resourceAsStream, new TypeReference<List<UbiSession>>() {});
  }

  public static void main(String[] args) throws IOException {

    List<UbiSession> ubiSessions = getUbiSessionList("/ExpectedData.json");

    System.out.print(ubiSessions.get(0));
  }

  enum RuleType {
    RULE1,
    RULE2,
    RULE3
  }
}
