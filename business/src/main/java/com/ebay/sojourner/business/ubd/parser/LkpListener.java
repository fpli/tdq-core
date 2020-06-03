package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.util.LkpManager;

public interface LkpListener {

  boolean notifyLkpChange(LkpManager lkpManager);

}
