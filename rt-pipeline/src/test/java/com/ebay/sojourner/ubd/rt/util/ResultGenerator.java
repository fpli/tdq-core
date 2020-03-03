package com.ebay.sojourner.ubd.rt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResultGenerator {

  public static List<UbiSession> getUbiSessionList(String fileName) throws IOException {
    InputStream resourceAsStream = RawEventGenerator.class.getResourceAsStream(fileName);
    InputStreamReader ir = new InputStreamReader(resourceAsStream);
    BufferedReader br = new BufferedReader(ir);
    String line = null;
    List<UbiSession> ubiSessions = new ArrayList<>();
    UbiSession ubiSession = null;
    while ((line = br.readLine()) != null) {

      JSONObject obj = JSON.parseObject(line);
      ubiSession = obj.getObject("UbiSession", UbiSession.class);

      ubiSessions.add(ubiSession);
    }

    return ubiSessions;
  }

  public static void main(String[] args) throws IOException {

    List<UbiSession> ubiSessions = getUbiSessionList("/ExpectedData");

    System.out.print(ubiSessions.get(0));
  }

  enum RuleType {
    RULE1,
    RULE2,
    RULE3
  }
}
