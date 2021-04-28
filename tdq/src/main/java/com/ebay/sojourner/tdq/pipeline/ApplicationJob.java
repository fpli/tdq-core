package com.ebay.sojourner.tdq.pipeline;

import com.ebay.tdq.Application;

/**
 * @author juntzhang
 */
public class ApplicationJob {
  public static void main(String[] args) throws Exception {
    new Application().start(args);
    // new Application().start(new String[]{"--profile", "qa"});
  }
}
