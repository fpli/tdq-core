package com.ebay.sojourner.flink.window;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class SojEventTimeSessionWindowsTest {

  @ClassRule
  public static MiniClusterWithClientResource flinkCluster =
      new MiniClusterWithClientResource(
          new MiniClusterResourceConfiguration.Builder()
              .setNumberSlotsPerTaskManager(1)
              .setNumberTaskManagers(1)
              .build());

  @Test
  public void testGap_NoMerging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    runTemplate(events, EventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(3, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(1, session0.getCount());
    Assert.assertEquals(1, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(10, session1.getStart());
    Assert.assertEquals(1, session1.getCount());
    Assert.assertEquals(4, session1.getSum());
    TestSession session2 = CollectSink.SESSIONS.get(2);
    Assert.assertEquals(20, session2.getStart());
    Assert.assertEquals(1, session2.getCount());
    Assert.assertEquals(16, session2.getSum());
  }

  @Test
  public void testGap_Merging_Ordered() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, EventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(6, session0.getCount());
    Assert.assertEquals(63, session0.getSum());
  }

  @Test
  public void testGap_Merging_Bridging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, EventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(6, session0.getCount());
    Assert.assertEquals(63, session0.getSum());
  }

  @Test
  public void testGap_Merging_LateEvent() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, EventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(10, session0.getStart());
    Assert.assertEquals(4, session0.getCount());
    Assert.assertEquals(60, session0.getSum());
  }

  @Test
  public void testDuration_Default_NoMerging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    runTemplate(events, SojEventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(3, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(1, session0.getCount());
    Assert.assertEquals(1, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(10, session1.getStart());
    Assert.assertEquals(1, session1.getCount());
    Assert.assertEquals(4, session1.getSum());
    TestSession session2 = CollectSink.SESSIONS.get(2);
    Assert.assertEquals(20, session2.getStart());
    Assert.assertEquals(1, session2.getCount());
    Assert.assertEquals(16, session2.getSum());
  }

  @Test
  public void testDuration_Default_Merging_Ordered() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(6, session0.getCount());
    Assert.assertEquals(63, session0.getSum());
  }

  @Test
  public void testDuration_Default_Merging_Bridging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(6, session0.getCount());
    Assert.assertEquals(63, session0.getSum());
  }

  @Test
  public void testDuration_Default_Merging_LateEvent() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows.withGap(Time.milliseconds(8L)));

    Assert.assertEquals(1, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(10, session0.getStart());
    Assert.assertEquals(4, session0.getCount());
    Assert.assertEquals(60, session0.getSum());
  }

  @Test
  public void testDuration_NonDefault_NoMerging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    runTemplate(events, SojEventTimeSessionWindows
        .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(15L)));

    Assert.assertEquals(3, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(1, session0.getCount());
    Assert.assertEquals(1, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(10, session1.getStart());
    Assert.assertEquals(1, session1.getCount());
    Assert.assertEquals(4, session1.getSum());
    TestSession session2 = CollectSink.SESSIONS.get(2);
    Assert.assertEquals(20, session2.getStart());
    Assert.assertEquals(1, session2.getCount());
    Assert.assertEquals(16, session2.getSum());
  }

  @Test
  public void testDuration_NonDefault_Merging_Ordered() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows
        .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(15L)));

    Assert.assertEquals(2, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(4, session0.getCount());
    Assert.assertEquals(15, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(20, session1.getStart());
    Assert.assertEquals(2, session1.getCount());
    Assert.assertEquals(48, session1.getSum());
  }

  @Test
  public void testDuration_NonDefault_Merging_Bridging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 2));
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows
        .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(15L)));

    Assert.assertEquals(2, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(4, session0.getCount());
    Assert.assertEquals(15, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(20, session1.getStart());
    Assert.assertEquals(2, session1.getCount());
    Assert.assertEquals(48, session1.getSum());
  }

  @Test
  public void testDuration_NonDefault_Merging_LateEvent() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 15L, 8));
    events.add(new TestEvent(1001, 10L, 4));
    events.add(new TestEvent(1001, 20L, 16));
    events.add(new TestEvent(1001, 25L, 32));
    runTemplate(events, SojEventTimeSessionWindows
        .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(15L)));

    Assert.assertEquals(2, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(10, session0.getStart());
    Assert.assertEquals(3, session0.getCount());
    Assert.assertEquals(28, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(25, session1.getStart());
    Assert.assertEquals(1, session1.getCount());
    Assert.assertEquals(32, session1.getSum());
  }

  @Test
  public void testDuration_NonDefault_Merging_Multi_Cut() {
    List<TestEvent> events = new ArrayList<>();
    events.add(new TestEvent(1001, 1L, 1));
    events.add(new TestEvent(1001, 5L, 1));
    events.add(new TestEvent(1001, 10L, 1));
    events.add(new TestEvent(1001, 15L, 1));
    // cut
    events.add(new TestEvent(1001, 20L, 1));
    events.add(new TestEvent(1001, 25L, 1));
    events.add(new TestEvent(1001, 30L, 1));
    // cut
    events.add(new TestEvent(1001, 35L, 1));
    events.add(new TestEvent(1001, 40L, 1));
    events.add(new TestEvent(1001, 45L, 1));
    // cut
    events.add(new TestEvent(1001, 50L, 1));
    events.add(new TestEvent(1001, 55L, 1));
    events.add(new TestEvent(1001, 60L, 1));
    // cut
    events.add(new TestEvent(1001, 65L, 1));
    events.add(new TestEvent(1001, 70L, 1));
    events.add(new TestEvent(1001, 75L, 1));
    // cut
    events.add(new TestEvent(1001, 80L, 1));
    events.add(new TestEvent(1001, 85L, 1));
    events.add(new TestEvent(1001, 90L, 1));
    // cut
    events.add(new TestEvent(1001, 95L, 1));
    runTemplate(events, SojEventTimeSessionWindows
        .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(15L)));

    Assert.assertEquals(7, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(1, session0.getStart());
    Assert.assertEquals(4, session0.getCount());
    Assert.assertEquals(4, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(20, session1.getStart());
    Assert.assertEquals(3, session1.getCount());
    Assert.assertEquals(3, session1.getSum());
    TestSession session6 = CollectSink.SESSIONS.get(6);
    Assert.assertEquals(95, session6.getStart());
    Assert.assertEquals(1, session6.getCount());
    Assert.assertEquals(1, session6.getSum());
  }

  @Test
  public void testInvalidSessionGap_Zero() {
    List<TestEvent> events = new ArrayList<>();
    try {
      runTemplate(events, SojEventTimeSessionWindows
          .withGapAndMaxDuration(Time.milliseconds(0L), Time.milliseconds(15L)));
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Session timeout must be larger than 0."));
    }
  }

  @Test
  public void testInvalidSessionGap_Negative() {
    List<TestEvent> events = new ArrayList<>();
    try {
      runTemplate(events, SojEventTimeSessionWindows
          .withGapAndMaxDuration(Time.milliseconds(-1L), Time.milliseconds(15L)));
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Session timeout must be larger than 0."));
    }
  }

  @Test
  public void testInvalidSessionMaxDuration_Zero() {
    List<TestEvent> events = new ArrayList<>();
    try {
      runTemplate(events, SojEventTimeSessionWindows
          .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(0L)));
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Session max duration must be larger than 0."));
    }
  }

  @Test
  public void testInvalidSessionMaxDuration_Negative() {
    List<TestEvent> events = new ArrayList<>();
    try {
      runTemplate(events, SojEventTimeSessionWindows
          .withGapAndMaxDuration(Time.milliseconds(8L), Time.milliseconds(-1L)));
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(e.getMessage().equals("Session max duration must be larger than 0."));
    }
  }

  public void runTemplate(List<TestEvent> events, WindowAssigner windowAssigner) {
    try {
      StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
      env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
      env.setParallelism(1);
      CollectSink.SESSIONS.clear();

      DataStream<TestEvent> source = env.addSource(new SourceFunction<TestEvent>() {
        private static final long serialVersionUID = 1L;

        @Override
        public void run(SourceContext<TestEvent> ctx) throws Exception {
          for (TestEvent event : events) {
            ctx.collectWithTimestamp(event, event.getTimestamp());
            ctx.emitWatermark(new Watermark(event.getTimestamp() - 10));
          }
          ctx.emitWatermark(new Watermark(Long.MAX_VALUE));
        }

        @Override
        public void cancel() {
        }
      });

      DataStream<TestSession> aggregated = source
          .keyBy(event -> event.getKey())
          .window(windowAssigner)
          .aggregate(new TestSessionAggregate());
      aggregated.addSink(new CollectSink());
      env.execute();
    } catch (Exception e) {
      Assert.fail();
    }
  }

}
