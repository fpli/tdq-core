package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

public interface Rule<T> {

    void init();
    int getBotFlag(T t);
}
