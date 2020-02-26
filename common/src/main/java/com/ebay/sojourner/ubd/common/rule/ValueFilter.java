package com.ebay.sojourner.ubd.common.rule;

public interface ValueFilter<Source, Expected> {

    boolean filter(Source source, Expected expected) throws Exception;

    void cleanup() throws Exception;
}
