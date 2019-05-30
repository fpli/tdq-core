package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

public interface Rule {
    public  void init();
    public   void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator);
    public  int getBotFlag();
    public  int getBotFlag(UbiSession ubiSession);
    public  void reset();


}
