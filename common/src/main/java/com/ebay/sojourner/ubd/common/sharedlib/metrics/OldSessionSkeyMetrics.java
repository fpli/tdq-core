package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

import java.util.HashSet;

public class OldSessionSkeyMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    public static final String OLD_SESSIONSKEY_DELIMITER = ",";

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().setOldSessionSkey(null);
        sessionAccumulator.getUbiSession().setOldSessionSkeySet(new HashSet<Long>());
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().getOldSessionSkeySet().add(event.getOldSessionSkey());

    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {
        if (!sessionAccumulator.getUbiSession().getOldSessionSkeySet().isEmpty()) {
            StringBuilder oldSessionSkeyBuilder = new StringBuilder();
            for (Long oldSessionSkey : sessionAccumulator.getUbiSession().getOldSessionSkeySet()) {
                oldSessionSkeyBuilder.append(oldSessionSkey).append(OLD_SESSIONSKEY_DELIMITER);
            }
            String value = oldSessionSkeyBuilder.toString();
            value = value.substring(0, value.length() - OLD_SESSIONSKEY_DELIMITER.length());
            sessionAccumulator.getUbiSession().setOldSessionSkey(value);
        }

    }

    @Override
    public void init() throws Exception {
        // nothing to do
    }
}
