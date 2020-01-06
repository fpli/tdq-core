package com.ebay.sojourner.ubd.common.sharedlib.indicators;


import com.ebay.sojourner.ubd.common.sharedlib.metrics.Aggregator;

/**
 * The aggregation 
 * @author kofeng
 *
 * @param <Source>
 * @param <Target>
 */
public interface Indicator<Source, Target> extends Aggregator<Source,Target> {

    /**
     * @param target
     */
    public boolean filter(Source source, Target target) throws Exception;
    /**
     * Feed the source to be aggregated for the target.
     * @param source
     * @param target
     */

    public void feed(Source source, Target target,boolean IsNeeded) throws Exception;
}
