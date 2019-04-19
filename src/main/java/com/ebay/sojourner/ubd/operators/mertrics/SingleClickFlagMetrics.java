package com.ebay.sojourner.ubd.operators.mertrics;


import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.model.UbiSession;

import java.util.HashSet;
import java.util.Set;

public class SingleClickFlagMetrics implements FieldMetrics<UbiEvent, UbiEvent> {
    


    @Override
    public void start(UbiEvent event, UbiEvent session) {
       Set<Integer> distinctClickIdSet = new HashSet<Integer>();
        UbiSession ubiSession = new UbiSession();
        ubiSession.setDistinctClickIdSet(distinctClickIdSet);
        event.setUbiSession(ubiSession);
        feed(event, session);
    }

    @Override
    public void feed(UbiEvent event, UbiEvent session) {
        if (event.getIframe() == 0) {
            Integer clickId = event.getClickId();
            if (event.getRdt() == 0) {
                if(clickId!=null) {
                    event.getUbiSession().getDistinctClickIdSet().add(clickId);
                }
            } else {
                if(clickId!=null&&event.getUbiSession().getDistinctClickIdSet().contains(clickId)) {
                    event.getUbiSession().getDistinctClickIdSet().remove(clickId);
                }
            }
        }
    }

    @Override
    public void end(UbiEvent session) {
        if (session.getUbiSession().getDistinctClickIdSet().size() == 1) {
            session.getUbiSession().setSingleClickSessionFlag(true);
        } else if (session.getUbiSession().getDistinctClickIdSet().size() == 0){
            session.getUbiSession().setSingleClickSessionFlag(null);
        } else {
            session.getUbiSession().setSingleClickSessionFlag(false);
        }
    }


}
