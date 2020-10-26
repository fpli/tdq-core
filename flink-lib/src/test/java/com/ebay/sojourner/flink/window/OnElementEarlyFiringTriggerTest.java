package com.ebay.sojourner.flink.window;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.BotSignature;
import java.io.Serializable;
import org.apache.flink.api.common.state.MergingState;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.State;
import org.apache.flink.api.common.state.StateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.windowing.triggers.Trigger.OnMergeContext;
import org.apache.flink.streaming.api.windowing.triggers.Trigger.TriggerContext;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnElementEarlyFiringTriggerTest {

  OnElementEarlyFiringTrigger trigger;

  @BeforeEach
  void setUp() {
    trigger = OnElementEarlyFiringTrigger.create();
  }

  @Test
  void create() {
    assertThat(trigger).isNotNull();
  }

  @Test
  void onElement() throws Exception {
    BotSignature botSignature = new BotSignature();
    trigger.onElement(botSignature, 1, new TimeWindow(0, 10), new TestTriggerContext());
  }

  @Test
  void onEventTime() throws Exception {
    trigger.onEventTime(1, new TimeWindow(0, 10), new TestTriggerContext());
  }

  @Test
  void onProcessingTime() throws Exception {
    trigger.onProcessingTime(1, new TimeWindow(0, 10), new TestTriggerContext());
  }

  @Test
  void clear() throws Exception {
    trigger.clear(new TimeWindow(0, 10L), new TestTriggerContext());
  }

  @Test
  void canMerge() {
    boolean result = trigger.canMerge();
    assertThat(result).isTrue();
  }

  @Test
  void onMerge() throws Exception {
    trigger.onMerge(new TimeWindow(0, 10), new OnMergeContext() {
      @Override
      public <S extends MergingState<?, ?>> void mergePartitionedState(StateDescriptor<S, ?> stateDescriptor) {

      }

      @Override
      public long getCurrentProcessingTime() {
        return 0;
      }

      @Override
      public MetricGroup getMetricGroup() {
        return null;
      }

      @Override
      public long getCurrentWatermark() {
        return 0;
      }

      @Override
      public void registerProcessingTimeTimer(long time) {

      }

      @Override
      public void registerEventTimeTimer(long time) {

      }

      @Override
      public void deleteProcessingTimeTimer(long time) {

      }

      @Override
      public void deleteEventTimeTimer(long time) {

      }

      @Override
      public <S extends State> S getPartitionedState(StateDescriptor<S, ?> stateDescriptor) {
        return null;
      }

      @Override
      public <S extends Serializable> ValueState<S> getKeyValueState(String name, Class<S> stateType, S defaultState) {
        return null;
      }

      @Override
      public <S extends Serializable> ValueState<S> getKeyValueState(String name, TypeInformation<S> stateType, S defaultState) {
        return null;
      }
    });
  }

  @Test
  void testToString() {
    trigger.toString();
  }

  class TestTriggerContext implements TriggerContext {

    @Override
    public long getCurrentProcessingTime() {
      return 0;
    }

    @Override
    public MetricGroup getMetricGroup() {
      return null;
    }

    @Override
    public long getCurrentWatermark() {
      return 0;
    }

    @Override
    public void registerProcessingTimeTimer(long time) {

    }

    @Override
    public void registerEventTimeTimer(long time) {

    }

    @Override
    public void deleteProcessingTimeTimer(long time) {

    }

    @Override
    public void deleteEventTimeTimer(long time) {

    }

    @Override
    public <S extends State> S getPartitionedState(StateDescriptor<S, ?> stateDescriptor) {

      ReducingState<Long> s = new ReducingState<Long>() {
        @Override
        public Long get() throws Exception {
          return 1L;
        }

        @Override
        public void add(Long value) throws Exception {

        }

        @Override
        public void clear() {

        }
      };
      return (S) s;
    }

    @Override
    public <S extends Serializable> ValueState<S> getKeyValueState(String name, Class<S> stateType, S defaultState) {
      return null;
    }

    @Override
    public <S extends Serializable> ValueState<S> getKeyValueState(String name, TypeInformation<S> stateType, S defaultState) {
      return null;
    }
  }
}