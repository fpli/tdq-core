package com.ebay.sojourner.ubd.common.sharedlib.parser;

/**
 * @author kofeng
 * @param <Source>
 */
public interface Parser<Source, Target> {

  void init() throws Exception;

  void parse(Source source, Target target) throws Exception;
}
