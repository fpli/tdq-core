package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

import java.util.Map;
import java.util.Set;

public class SingleClickFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

    @Override
    public void init() throws Exception {
        // nothing to do
    }

    @Override
    public void start(SessionAccumulator sessionAccumulator) {
        sessionAccumulator.getUbiSession().getDistinctClickIdSet().clear();
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {

        if (!event.isIframe()) {
            int clickId = event.getClickId();
            if (!event.isRdt()) {
                if (clickId != -1) {
                    Set<Integer> distinctClickIdSet
                            = sessionAccumulator.getUbiSession().getDistinctClickIdSet();
                    if (distinctClickIdSet != null && distinctClickIdSet.size() < 10) {
                        Map<Integer, Long> clickWithStamp
                                = sessionAccumulator.getUbiSession().getClickWithStamp();
                        if (clickWithStamp.get(clickId) == null
                                || (clickWithStamp.get(clickId) != null &&
                                clickWithStamp.get(clickId) < event.getEventTimestamp())) {
                            clickWithStamp.put(clickId, event.getEventTimestamp());
                        }
                        distinctClickIdSet.add(clickId);
                    }
                }
            } else {
                if (clickId != -1) {
                    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(
                            event.getEventTimestamp(),
                            sessionAccumulator.getUbiSession().getClickWithStamp().get(clickId));
                    if (!isEarlyValidEvent) {
                        sessionAccumulator.getUbiSession().getDistinctClickIdSet().remove(clickId);
                    }
                }
            }
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) {
        if (sessionAccumulator.getUbiSession().getDistinctClickIdSet().size() == 1) {
            sessionAccumulator.getUbiSession().setSingleClickSessionFlag(true);
        } else if (sessionAccumulator.getUbiSession().getDistinctClickIdSet().size() == 0) {
            sessionAccumulator.getUbiSession().setSingleClickSessionFlag(null);
        } else {
            sessionAccumulator.getUbiSession().setSingleClickSessionFlag(false);
        }
    }
}
