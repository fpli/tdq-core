package com.ebay.tdq.planner;

import java.io.Serializable;

/**
 * @author juntzhang
 */
public interface Refreshable extends Serializable {

  void refresh();

  void start();

  void stop();
}
