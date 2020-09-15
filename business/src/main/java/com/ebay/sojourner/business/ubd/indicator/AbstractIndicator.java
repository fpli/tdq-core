package com.ebay.sojourner.business.ubd.indicator;

import com.ebay.sojourner.business.ubd.metric.Aggregator;
import com.ebay.sojourner.common.util.BotFilter;

/**
 * The aggregation
 *
 * @author kofeng
 */
public abstract class AbstractIndicator<Source, Target> implements Aggregator<Source, Target> {

  protected BotFilter botFilter;

  /**
   *
   */
  abstract boolean filter(Source source, Target target) throws Exception;

  @Override
  public void init() throws Exception {
    // empty implementation
  }

  @Override
  public void start(Target target) throws Exception {
    // empty implementation
  }

  @Override
  public void feed(Source source, Target target) throws Exception {
    // empty implementation
  }

  @Override
  public void end(Target target) throws Exception {
    // empty implementation
  }
}
