package com.ebay.sojourner.tdq.pipeline;

import com.ebay.tdq.ProfilingJob;

/**
 * @author juntzhang
 */
public class ApplicationJob {
  public static void main(String[] args) throws Exception {
    new ProfilingJob().start(args);
  }
}
