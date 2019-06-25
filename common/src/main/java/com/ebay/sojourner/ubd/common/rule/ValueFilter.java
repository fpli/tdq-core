package com.ebay.sojourner.ubd.common.rule;

public interface ValueFilter<Source, Expected> {
    
    public boolean filter(Source source, Expected expected) throws Exception;
    
    public void cleanup() throws Exception;
}
