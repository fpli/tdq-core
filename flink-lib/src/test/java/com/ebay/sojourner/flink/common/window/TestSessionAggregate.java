package com.ebay.sojourner.flink.common.window;

import org.apache.flink.api.common.functions.AggregateFunction;

public class TestSessionAggregate implements
    AggregateFunction<TestEvent, TestSession, TestSession> {

  @Override
  public TestSession createAccumulator() {
    return new TestSession();
  }

  @Override
  public TestSession add(
      TestEvent event, TestSession session) {
    session.setCount(session.getCount() + 1);
    session.setSum(session.getSum() + event.getValue());
    if (session.getStart() < 0 || event.getTimestamp() < session.getStart()) {
      session.setStart(event.getTimestamp());
    }
    return session;
  }

  @Override
  public TestSession getResult(
      TestSession session) {
    return session;
  }

  @Override
  public TestSession merge(
      TestSession a, TestSession b) {
    a.setCount(a.getCount() + b.getCount());
    a.setSum(a.getSum() + b.getSum());
    if (a.getStart() > b.getStart()) {
      a.setStart(b.getStart());
    }
    return a;
  }
}
