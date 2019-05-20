package com.ebay.sojourner.ubd.sojlib.parser;



/**
 * @author kofeng
 *
 * @param <Source>
 */
public interface Parser<Source, Target,Configuration, Context> {


    public void init(Configuration configuration,Context context) throws Exception ;
    public void parse(Source source, Target target) throws Exception;
}
