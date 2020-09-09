package com.ebay.sojourner.flink.common.window;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class CollectSink implements SinkFunction<TestSession> {
  // must be static
  public static final List<TestSession> SESSIONS = new ArrayList<>();

  @Override
  public synchronized void invoke(TestSession session) throws Exception {
    SESSIONS.add(session);
  }
}
