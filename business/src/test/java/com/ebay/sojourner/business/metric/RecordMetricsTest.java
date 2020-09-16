package com.ebay.sojourner.business.metric;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecordMetricsTest {

  RecordMetrics<String, String> recordMetrics;

  @BeforeEach
  void setUp() {
    recordMetrics = new TestRecordMetrics();
    recordMetrics.fieldMetrics.add(new TestFieldMetrics());
  }

  @Test
  void test_init() throws Exception {
    recordMetrics.init();
  }

  @Test
  void test_start() throws Exception {
    recordMetrics.start("bla");
  }

  @Test
  void test_feed() throws Exception {
    recordMetrics.feed("bla", "bla");
  }

  @Test
  void test_end() throws Exception {
    recordMetrics.end("bla");
  }

  class TestRecordMetrics extends RecordMetrics<String, String> {

    @Override
    public void initFieldMetrics() {
      //            addFieldMetrics(new TestFieldMetrics());
    }
  }

  class TestFieldMetrics implements FieldMetrics<String, String> {

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(String s) throws Exception {
    }

    @Override
    public void feed(String s, String s2) throws Exception {
    }

    @Override
    public void end(String s) throws Exception {
    }
  }
}
