package com.ebay.sojourner.business.ubd.indicator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributeIndicatorsTest {

  AttributeIndicators<String, String> attributeIndicators;

  @BeforeEach
  void setup() {
    attributeIndicators = new TestAttributeIndicators();
    attributeIndicators.indicators.add(new TestAbstractIndicator());
  }

  @Test
  void test_init() throws Exception {
    attributeIndicators.init();
  }

  @Test
  void test_start() throws Exception {
    attributeIndicators.start("bla");
  }

  @Test
  void test_feed_isNeeded() throws Exception {
    attributeIndicators.feed("test", "test");
  }

  @Test
  void test_feed() throws Exception {
    attributeIndicators.feed("test", "test");
  }

  @Test
  void test_end() throws Exception {
    attributeIndicators.end("test");
  }

  class TestAttributeIndicators extends AttributeIndicators<String, String> {

    @Override
    public void initIndicators() {
    }
  }

  class TestAbstractIndicator extends AbstractIndicator<String, String> {

    @Override
    boolean filter(String s, String s2) throws Exception {
      return false;
    }

    @Override
    public  void feed(String s, String s2) throws Exception {
    }
  }
}
