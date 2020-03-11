package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;

/**
 * @author weifang.
 */
public interface BotFilter {

  // return true means the session should be filtered
  boolean filter(UbiSession ubiSession, Integer targetFlag);
}
