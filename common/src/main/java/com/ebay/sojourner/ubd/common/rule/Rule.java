package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

public interface Rule {
    void init();

    void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator);

    int getBotFlag();

    int getBotFlag(UbiSession ubiSession);

    void reset();


}
