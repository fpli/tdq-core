package com.ebay.sojourner.business.ubd.parser;

/**
 * @author kofeng
 */
public interface Parser<Source, Target> {

  void init() throws Exception;

  void parse(Source source, Target target) throws Exception;
}
