package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.Aggregator;
import com.ebay.sojourner.ubd.common.util.BotFilter;

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

  /**
   * Feed the source to be aggregated for the target.
   */
  abstract void feed(Source source, Target target, boolean isNeeded) throws Exception;

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
