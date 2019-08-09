package com.ebay.sojourner.ubd.common.util;

import java.io.IOException;

public interface Filter<Source> {

    public boolean filter(Source source) throws IOException, InterruptedException;

    public void cleanup() throws IOException, InterruptedException;
}
