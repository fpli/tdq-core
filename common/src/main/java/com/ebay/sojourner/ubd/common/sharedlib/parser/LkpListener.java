package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.util.LkpManager;

public interface LkpListener {

  boolean notifyLkpChange(LkpManager lkpManager);

}
