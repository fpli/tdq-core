package com.ebay.sojourner.ubd.operators.mertrics;


/**
 * The aggregation 
 * @author kofeng
 *
 * @param <Source>
 * @param <Target>
 */
public interface Aggregator<Source, Target> {

    /**
     * Start is the start point to aggregate the source for the target.
     * @param source
     * @param target
     */
    public void start(Source source, Target target) throws Exception;

    /**
     * Feed the source to be aggregated for the target. 
     * @param source
     * @param target
     */
    public void feed(Source source, Target target) throws Exception;
    
    /**
     * @param target
     */
    public void end(Target target) throws Exception;
}
