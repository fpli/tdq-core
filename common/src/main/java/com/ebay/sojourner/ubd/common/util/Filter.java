package com.ebay.sojourner.ubd.common.util;

import java.io.IOException;

public interface Filter<Source> {

  boolean filter(Source source) throws IOException, InterruptedException;

  void cleanup() throws IOException, InterruptedException;
}
