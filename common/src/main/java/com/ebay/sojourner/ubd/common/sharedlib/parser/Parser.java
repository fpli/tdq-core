package com.ebay.sojourner.ubd.common.sharedlib.parser;



/**
 * @author kofeng
 *
 * @param <Source>
 */
public interface Parser<Source, Target> {


    public void init() throws Exception ;
    public void parse(Source source, Target target) throws Exception;
}
