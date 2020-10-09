package com.ebay.sojourner.flink.window;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class CompositeTriggerTest {

  public static final LocalDateTime SOME_MIDNIGHT =
      LocalDateTime.of(LocalDate.of(2020, 9, 1), LocalTime.MIDNIGHT);
  public static final ZoneOffset ZONE_OFFSET_MST = ZoneOffset.ofHours(-7);

  @ClassRule
  public static MiniClusterWithClientResource flinkCluster =
      new MiniClusterWithClientResource(
          new MiniClusterResourceConfiguration.Builder()
              .setNumberSlotsPerTaskManager(1)
              .setNumberTaskManagers(1)
              .build());

  @Test
  public void testMidnightTrigger_NoMerging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(
        new TestEvent(1001,
            SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            1));
    events.add(
        new TestEvent(1001,
            SOME_MIDNIGHT.plusMinutes(30).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            2));
    runTemplate(events, CompositeTrigger.Builder.create()
        .trigger(MidnightOpenSessionTrigger.of(Time.hours(7)))
        .trigger(EventTimeTrigger.create()).build());

    Assert.assertEquals(3, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        session0.getStart());
    Assert.assertEquals(1, session0.getCount());
    Assert.assertEquals(1, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        session1.getStart());
    Assert.assertEquals(1, session1.getCount());
    Assert.assertEquals(1, session1.getSum());
    TestSession session2 = CollectSink.SESSIONS.get(2);
    Assert.assertEquals(SOME_MIDNIGHT.plusMinutes(30).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        session2.getStart());
    Assert.assertEquals(1, session2.getCount());
    Assert.assertEquals(2, session2.getSum());
  }

  @Test
  public void testMidnightTrigger_Merging() {
    List<TestEvent> events = new ArrayList<>();
    events.add(
        new TestEvent(1001,
            SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            1));
    events.add(
        new TestEvent(1001,
            SOME_MIDNIGHT.plusMinutes(1).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            2));
    events.add(
        new TestEvent(1001,
            SOME_MIDNIGHT.plusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
            4));
    runTemplate(events, CompositeTrigger.Builder.create()
        .trigger(MidnightOpenSessionTrigger.of(Time.hours(7)))
        .trigger(EventTimeTrigger.create()).build());

    Assert.assertEquals(2, CollectSink.SESSIONS.size());
    TestSession session0 = CollectSink.SESSIONS.get(0);
    Assert.assertEquals(SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        session0.getStart());
    Assert.assertEquals(2, session0.getCount());
    Assert.assertEquals(3, session0.getSum());
    TestSession session1 = CollectSink.SESSIONS.get(1);
    Assert.assertEquals(SOME_MIDNIGHT.minusMinutes(10).toEpochSecond(ZONE_OFFSET_MST) * 1000,
        session1.getStart());
    Assert.assertEquals(3, session1.getCount());
    Assert.assertEquals(7, session1.getSum());
  }

  public void runTemplate(List<TestEvent> events, Trigger trigger) {
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
          .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
          .trigger(trigger)
          .aggregate(new TestSessionAggregate());
      aggregated.addSink(new CollectSink());
      env.execute();
    } catch (Exception e) {
      Assert.fail();
    }
  }
}
