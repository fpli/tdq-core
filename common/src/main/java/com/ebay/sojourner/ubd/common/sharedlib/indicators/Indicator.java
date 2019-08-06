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
}
