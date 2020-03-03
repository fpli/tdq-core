package com.ebay.sojourner.ubd.common.sharedlib.metrics;

/**
 * The aggregation
 *
 * @author kofeng
 * @param <Source>
 * @param <Target>
 */
public interface Aggregator<Source, Target> {
  /** init the aggregator */
  void init() throws Exception;
  /**
   * Start is the start point to aggregate the source for the target.
   *
   * @param target
   */
  void start(Target target) throws Exception;

  /**
   * Feed the source to be aggregated for the target.
   *
   * @param source
   * @param target
   */
  void feed(Source source, Target target) throws Exception;

  /** @param target */
  void end(Target target) throws Exception;
}
